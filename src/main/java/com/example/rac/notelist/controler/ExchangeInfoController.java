package com.example.rac.notelist.controler;

import com.example.rac.notelist.arb.Arbitrshe;
import com.example.rac.notelist.entity.ArbitrageData;
import com.example.rac.notelist.entity.ChainInfo;
import com.example.rac.notelist.entity.ExchangeInfo;
import com.example.rac.notelist.entity.User;
import com.example.rac.notelist.serves.ExchangeInfoService;
import com.example.rac.notelist.serves.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exchange")
public class ExchangeInfoController {

    private final ExchangeInfoService exchangeInfoService;

    private final UserService userService;

    @Autowired
    public ExchangeInfoController(ExchangeInfoService exchangeInfoService, UserService userService) {
        this.exchangeInfoService = exchangeInfoService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public String listExchangeInfos(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.findByUsername(currentUsername);

        List<ExchangeInfo> exchangeInfos = exchangeInfoService.findByUserId(currentUser.getId());
        model.addAttribute("exchangeInfos", exchangeInfos);
        model.addAttribute("arbitrageData", new ArbitrageData()); // Add ArbitrageData attribute
        return "exchangeInfoList";
    }

    @GetMapping("/arbitrage")
    public String arbitrageForm(Model model) {
        model.addAttribute("arbitrageData", new ArbitrageData());
        return "arbitrageResult";
    }

    @PostMapping("/arbitrage")
    public String performArbitrage(@ModelAttribute("arbitrageData") ArbitrageData arbitrageData, Model model) throws IOException, ExecutionException, InterruptedException {
        // Логи для отладки

        Arbitrshe arbitrshe = new Arbitrshe();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.findByUsername(currentUsername);

        // Проверка, принадлежат ли выбранные API текущему пользователю
        List<ExchangeInfo> exchangeInfos = exchangeInfoService.findByUserId(currentUser.getId());
        List<ExchangeInfo> selectedApi1 = exchangeInfos.stream()
                .filter(info -> info.getUsername().equals(arbitrageData.getUsername()))
                .collect(Collectors.toList());
        List<ExchangeInfo> selectedApi2 = exchangeInfos.stream()
                .filter(info -> info.getUsername().equals(arbitrageData.getUsername2()))
                .collect(Collectors.toList());

        if (selectedApi1.isEmpty() || selectedApi2.isEmpty()) {
            // Если API не принадлежат пользователю, возвращаем ошибку
            model.addAttribute("error", "Выбранные API не принадлежат текущему пользователю.");
            model.addAttribute("arbitrageData", arbitrageData);
            model.addAttribute("exchangeInfos", exchangeInfos);
            return "arbitrageResult";
        }

        // Добавляем выбранные API в модель
        model.addAttribute("selectedApi1", selectedApi1.get(0));
        model.addAttribute("selectedApi2", selectedApi2.get(0));

        List<ChainInfo> c = arbitrshe.getMatchingChainsmain(selectedApi1.get(0),arbitrageData.getCryptoName());
        List<ChainInfo> d = arbitrshe.getMatchingChainsmain(selectedApi1.get(0),"USDT");
        List<ChainInfo> e = arbitrshe.getMatchingChainsmain(selectedApi2.get(0),arbitrageData.getCryptoName());
        List<ChainInfo> f = arbitrshe.getMatchingChainsmain(selectedApi2.get(0),"USDT");

        System.out.println(arbitrageData.getCryptoName());
        List<ChainInfo> a = arbitrshe.getMatchingChains(selectedApi1.get(0), selectedApi2.get(0), arbitrageData.getCryptoName());
        List<ChainInfo> b = arbitrshe.getMatchingChains(selectedApi2.get(0), selectedApi1.get(0), "USDT");



        // Добавляем списки a и b в модель
        model.addAttribute("listA", a);
        model.addAttribute("listB", b);


        model.addAttribute("listC", c);
        model.addAttribute("listD", d);
        model.addAttribute("listE", e);
        model.addAttribute("listF", f);
        return "arbitrageResult";
    }



    @GetMapping("/add")
    public String addExchangeInfoForm(Model model) {
        model.addAttribute("exchangeInfo", new ExchangeInfo());
        return "exchangeInfoAdd";
    }

    @PostMapping("/add")
    public String addExchangeInfo(@ModelAttribute("exchangeInfo") ExchangeInfo exchangeInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.findByUsername(currentUsername);

        exchangeInfo.setUser(currentUser); // Установка текущего пользователя
        exchangeInfoService.add(exchangeInfo);
        return "redirect:/exchange/list";
    }

    @GetMapping("/edit")
    public String editExchangeInfoForm(@RequestParam("id") Long id, Model model) {
        Optional<ExchangeInfo> exchangeInfo = exchangeInfoService.getById(id);
        model.addAttribute("exchangeInfo", exchangeInfo.orElse(null));
        return "exchangeInfoEdit";
    }

    @PostMapping("/edit")
    public String editExchangeInfo(@ModelAttribute("exchangeInfo") ExchangeInfo exchangeInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.findByUsername(currentUsername);

        exchangeInfo.setUser(currentUser); // Установка текущего пользователя
        exchangeInfoService.update(exchangeInfo);
        return "redirect:/exchange/list";
    }

    @PostMapping("/delete")
    public String deleteExchangeInfo(@RequestParam("id") Long id) {
        exchangeInfoService.deleteById(id);
        return "redirect:/exchange/list";
    }
}
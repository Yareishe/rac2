package com.example.rac.notelist.controler;


import com.example.rac.notelist.entity.User;
import com.example.rac.notelist.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping("/")
public class RegistrationControler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationControler(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registration")
    public String getRegistration(User user, Model model){
        model.addAttribute("user", user);
        System.out.println( user + " 1 "  + model);
        return "registration";
    }
    @PostMapping("/registration")
    public String successRegistration(@ModelAttribute("user")User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        System.out.println( user + " 2 ");
        return "redirect:/login";
    }
}
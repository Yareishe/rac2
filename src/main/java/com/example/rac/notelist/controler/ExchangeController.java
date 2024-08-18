package com.example.rac.notelist.controler;

import com.example.rac.notelist.entity.MyUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeController {

    @GetMapping("/user/exchange")
    public String getUserExchangeInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof MyUserDetails) {
                Long userId = ((MyUserDetails) principal).getId();
                // Логика для получения информации о бирже для данного user_id
                return "Информация о бирже для пользователя с id: " + userId;
            }
        }
        return "Пользователь не аутентифицирован";
    }
}
package com.summer.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Renjun Yu
 * @date 2024/07/21 14:56
 */
@Controller
public class LoginController {
    @GetMapping("/login")
    String login() {
        return "login";
    }
}

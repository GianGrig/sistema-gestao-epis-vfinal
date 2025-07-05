package com.app.home; // ou o pacote onde estão seus controllers

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // Isso diz ao Spring para renderizar o arquivo "index.html"
    }
}
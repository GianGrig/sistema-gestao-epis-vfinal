package com.app.menu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @GetMapping("/adm")
    public String menuAdm() {
        return "menu/menu_adm";
    }

    @GetMapping("/colaborador")
    public String menuColaborador() {
        return "menu/menu_colaborador";
    }
}


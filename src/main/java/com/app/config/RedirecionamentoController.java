package com.app.config;

import com.app.security.UsuarioDetails;
import com.app.usuario.Perfil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirecionamentoController {

    @GetMapping("/redirecionar")
    public String redirecionar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsuarioDetails details = (UsuarioDetails) auth.getPrincipal();
        Perfil perfil = details.getPerfil();

        if (perfil == Perfil.ADMINISTRADOR) {
            return "redirect:/menu/adm";
        } else {
            return "redirect:/menu/colaborador";
        }
    }
}

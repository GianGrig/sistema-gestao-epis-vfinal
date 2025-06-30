package com.app.login;

import com.app.usuario.Perfil;
import com.app.usuario.Usuario;
import com.app.usuario.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class LoginController {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/login")
    public String mostrarFormulario() {
        return "login/login";
    }

    @PostMapping("/login")
    public void processarLogin(@RequestParam String email,
                               @RequestParam String senha,
                               @RequestParam String perfil,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        Login login = new Login();
        login.setEmail(email);
        login.setSenha(senha);
        login.setPerfil(Perfil.valueOf(perfil));

        if (loginRepository.autenticar(login)) {
            Usuario usuario = usuarioRepository.buscarPorEmail(email);
            request.getSession().setAttribute("usuarioLogado", usuario); // ✅ Salva usuário logado

            if (login.getPerfil() == Perfil.ADMINISTRADOR) {
                response.sendRedirect("/html/menu/menu_adm.html");
            } else {
                response.sendRedirect("/html/menu/menu_colaborador.html");
            }
        } else {
            response.sendRedirect("/html/login/login.html?erro=1");
        }
    }

}

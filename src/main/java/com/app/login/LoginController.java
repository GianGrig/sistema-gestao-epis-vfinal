package com.app.login;

import com.app.usuario.Perfil;
import com.app.usuario.Usuario;
import com.app.usuario.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Controlador responsável por exibir e processar o formulário de login.
 */
@Controller
public class LoginController {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Exibe a tela de login.
     */
    @GetMapping("/login")
    public String mostrarFormulario() {
        return "login/login";
    }

    /**
     * Processa os dados enviados pelo formulário de login.
     */
    @PostMapping("/login")
    public void processarLogin(@RequestParam String email,
                               @RequestParam String senha,
                               @RequestParam String perfil,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {

        // Cria o objeto de login com os dados fornecidos
        Login login = new Login();
        login.setEmail(email);
        login.setSenha(senha);
        login.setPerfil(Perfil.valueOf(perfil));

        // Valida as credenciais
        if (loginRepository.autenticar(login)) {
            Usuario usuario = usuarioRepository.buscarPorEmail(email);

            // Armazena o usuário logado na sessão
            request.getSession().setAttribute("usuarioLogado", usuario);

            // Redireciona para o menu conforme o perfil
            if (login.getPerfil() == Perfil.ADMINISTRADOR) {
                response.sendRedirect("/html/menu/menu_adm.html");
            } else {
                response.sendRedirect("/html/menu/menu_colaborador.html");
            }
        } else {
            // Redireciona para o login com erro
            response.sendRedirect("/html/login/login.html?erro=1");
        }
    }
}

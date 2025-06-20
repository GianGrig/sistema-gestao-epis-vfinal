package com.app.usuario;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/usuario")
    public String salvar(@RequestParam String nome,
                         @RequestParam String email,
                         @RequestParam String senha,
                         @RequestParam Perfil perfil) {

        if (nome.length() < 2 || nome.length() > 100) {
            return redirectComMensagemErro("O nome deve ter entre 2 e 100 caracteres.");
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            return redirectComMensagemErro("Email inválido.");
        }

        if (!senha.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$")) {
            return redirectComMensagemErro("A senha deve ter ao menos 6 caracteres, incluindo maiúsculas, minúsculas, número e caractere especial.");
        }

        nome = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();

        usuarioRepository.salvar(new Usuario(nome, email, senha, perfil));
        return redirectComMensagemSucesso("Usuário cadastrado com sucesso.");
    }

    @GetMapping("/usuario")
    @ResponseBody
    public List<Usuario> listar() {
        return usuarioRepository.buscarTodos();
    }

    @PostMapping("/usuario/atualizar")
    public String atualizar(@RequestParam int id_usuario,
                            @RequestParam String nome,
                            @RequestParam String email,
                            @RequestParam String senha,
                            @RequestParam Perfil perfil) {

        Usuario existente = usuarioRepository.buscarPorId(id_usuario);
        if (existente == null) {
            return redirectComMensagemErro("Usuário com ID " + id_usuario + " não encontrado.");
        }

        if (nome.length() < 2 || nome.length() > 100) {
            return redirectComMensagemErro("O nome deve ter entre 2 e 100 caracteres.");
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            return redirectComMensagemErro("Email inválido.");
        }

        if (!senha.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$")) {
            return redirectComMensagemErro("A senha deve ter ao menos 6 caracteres, incluindo maiúsculas, minúsculas, número e caractere especial.");
        }

        nome = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();

        usuarioRepository.atualizar(new Usuario(id_usuario, nome, email, senha, perfil));
        return redirectComMensagemSucesso("Usuário atualizado com sucesso.");
    }

    @PostMapping("/usuario/deletar")
    public String deletar(@RequestParam int id_usuario) {
        Usuario existente = usuarioRepository.buscarPorId(id_usuario);
        if (existente == null) {
            return redirectComMensagemErro("Usuário com ID " + id_usuario + " não encontrado.");
        }

        if (usuarioRepository.estaVinculadoAEmprestimo(id_usuario)) {
            return redirectComMensagemErro("O usuário está vinculado a um empréstimo e não pode ser excluído.");
        }

        usuarioRepository.deletar(id_usuario);
        return redirectComMensagemSucesso("Usuário excluído com sucesso.");
    }

    // Utilitários para mensagens
    private String redirectComMensagemErro(String mensagem) {
        return "redirect:/usuario/form_usuario.html?erro=" + encode(mensagem);
    }

    private String redirectComMensagemSucesso(String mensagem) {
        return "redirect:/usuario/form_usuario.html?sucesso=" + encode(mensagem);
    }


    private String encode(String mensagem) {
        try {
            return URLEncoder.encode(mensagem, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return mensagem;
        }
    }
}

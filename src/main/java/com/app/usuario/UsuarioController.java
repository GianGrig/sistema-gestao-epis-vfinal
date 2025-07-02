package com.app.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder;

    @PostMapping
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

        if (usuarioRepository.buscarPorEmail(email) != null) {
            return redirectComMensagemErro("Este e-mail já está cadastrado.");
        }

        if (!senha.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{6,}$")) {
            return redirectComMensagemErro("A senha deve ter ao menos 6 caracteres, incluindo maiúsculas, minúsculas, número e caractere especial.");
        }

        nome = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();

        String senhaCriptografada = passwordEncoder.encode(senha);
        usuarioRepository.salvar(new Usuario(nome, email, senhaCriptografada, perfil));

        return redirectComMensagemSucesso("Usuário cadastrado com sucesso.");
    }

    @PostMapping("/atualizar")
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

        Usuario outroUsuario = usuarioRepository.buscarPorEmail(email);
        if (outroUsuario != null && outroUsuario.getId_usuario() != id_usuario) {
            return redirectComMensagemErro("Este e-mail já está cadastrado por outro usuário.");
        }

        if (!senha.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{6,}$")) {
            return redirectComMensagemErro("A senha deve ter ao menos 6 caracteres, incluindo maiúsculas, minúsculas, número e caractere especial.");
        }

        nome = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();
        String senhaCriptografada = passwordEncoder.encode(senha);

        usuarioRepository.atualizar(new Usuario(id_usuario, nome, email, senhaCriptografada, perfil));
        return redirectComMensagemSucesso("Usuário atualizado com sucesso.");
    }

    @PostMapping("/deletar")
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

    // PÁGINAS
    @GetMapping("/form")
    public String exibirFormulario() {
        return "usuario/form_usuario";
    }

    @GetMapping("/listar")
    public String listarUsuarios() {
        return "usuario/listar_usuario";
    }

    // JSON
    @GetMapping("/todos")
    @ResponseBody
    public List<Usuario> listarJson() {
        return usuarioRepository.buscarTodos();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Usuario buscarPorId(@PathVariable int id) {
        Usuario usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com ID " + id + " não encontrado.");
        }
        return usuario;
    }

    // REDIRECIONAMENTO
    private String redirectComMensagemErro(String mensagem) {
        return "redirect:/usuario/form?erro=" + encode(mensagem);
    }

    private String redirectComMensagemSucesso(String mensagem) {
        return "redirect:/usuario/form?sucesso=" + encode(mensagem);
    }

    private String encode(String mensagem) {
        try {
            return URLEncoder.encode(mensagem, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return mensagem;
        }
    }
}


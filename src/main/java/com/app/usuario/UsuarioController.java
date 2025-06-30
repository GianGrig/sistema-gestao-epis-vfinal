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

/**
 * Controlador responsável pelas operações web relacionadas ao usuário.
 */
@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Cadastra um novo usuário com validações.
     */
    @PostMapping("/html/usuario")
    public String salvar(@RequestParam String nome,
                         @RequestParam String email,
                         @RequestParam String senha,
                         @RequestParam Perfil perfil) {

        // Validação: nome deve ter entre 2 e 100 caracteres
        if (nome.length() < 2 || nome.length() > 100) {
            return redirectComMensagemErro("O nome deve ter entre 2 e 100 caracteres.");
        }

        // Validação: formato de email válido (ex: usuario@dominio.com)
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            return redirectComMensagemErro("Email inválido.");
        }

        // Validação: e-mail não pode estar cadastrado no banco de dados
        if (usuarioRepository.buscarPorEmail(email) != null) {
            return redirectComMensagemErro("Este e-mail já está cadastrado.");
        }

        // Validação: senha forte (mín. 6 caracteres com letra maiúscula, minúscula, número e caractere especial)
        if (!senha.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$")) {
            return redirectComMensagemErro("A senha deve ter ao menos 6 caracteres, incluindo maiúsculas, minúsculas, número e caractere especial.");
        }

        // Capitaliza o nome (primeira letra maiúscula, restante minúsculo)
        nome = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();

        // Salva o usuário no banco
        usuarioRepository.salvar(new Usuario(nome, email, senha, perfil));
        return redirectComMensagemSucesso("Usuário cadastrado com sucesso.");
    }

    /**
     * Retorna todos os usuários cadastrados em JSON.
     */
    @GetMapping("/html/usuario")
    @ResponseBody
    public List<Usuario> listar() {
        return usuarioRepository.buscarTodos();
    }

    /**
     * Atualiza os dados de um usuário existente com validações.
     */
    @PostMapping("/html/usuario/atualizar")
    public String atualizar(@RequestParam int id_usuario,
                            @RequestParam String nome,
                            @RequestParam String email,
                            @RequestParam String senha,
                            @RequestParam Perfil perfil) {

        // Verifica se o usuário existe
        Usuario existente = usuarioRepository.buscarPorId(id_usuario);
        if (existente == null) {
            return redirectComMensagemErro("Usuário com ID " + id_usuario + " não encontrado.");
        }

        // Validação: nome com tamanho adequado
        if (nome.length() < 2 || nome.length() > 100) {
            return redirectComMensagemErro("O nome deve ter entre 2 e 100 caracteres.");
        }

        // Validação: formato de e-mail válido
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            return redirectComMensagemErro("Email inválido.");
        }

        // Validação: verificar se o novo e-mail já está sendo usado por outro usuário
        Usuario outroUsuario = usuarioRepository.buscarPorEmail(email);
        if (outroUsuario != null && outroUsuario.getId_usuario() != id_usuario) {
            return redirectComMensagemErro("Este e-mail já está cadastrado por outro usuário.");
        }

        // Validação: senha segura com todos os critérios
        if (!senha.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$")) {
            return redirectComMensagemErro("A senha deve ter ao menos 6 caracteres, incluindo maiúsculas, minúsculas, número e caractere especial.");
        }

        // Formata nome
        nome = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();

        // Atualiza usuário no banco
        usuarioRepository.atualizar(new Usuario(id_usuario, nome, email, senha, perfil));
        return redirectComMensagemSucesso("Usuário atualizado com sucesso.");
    }

    /**
     * Busca um usuário pelo ID e retorna como JSON.
     */
    @GetMapping("/html/usuarios/{id}")
    @ResponseBody
    public Usuario buscarPorId(@PathVariable int id) {
        Usuario usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com ID " + id + " não encontrado.");
        }
        return usuario;
    }

    /**
     * Exclui um usuário, caso ele exista e não esteja vinculado a empréstimos.
     */
    @PostMapping("/html/usuario/deletar")
    public String deletar(@RequestParam int id_usuario) {
        // Verifica se o usuário existe
        Usuario existente = usuarioRepository.buscarPorId(id_usuario);
        if (existente == null) {
            return redirectComMensagemErro("Usuário com ID " + id_usuario + " não encontrado.");
        }

        // Verifica se o usuário está vinculado a algum empréstimo (não pode excluir se estiver)
        if (usuarioRepository.estaVinculadoAEmprestimo(id_usuario)) {
            return redirectComMensagemErro("O usuário está vinculado a um empréstimo e não pode ser excluído.");
        }

        // Deleta o usuário
        usuarioRepository.deletar(id_usuario);
        return redirectComMensagemSucesso("Usuário excluído com sucesso.");
    }

    // Métodos utilitários para redirecionamento com mensagens

    /**
     * Redireciona com mensagem de erro (parâmetro ?erro= na URL).
     */
    private String redirectComMensagemErro(String mensagem) {
        return "redirect:/html/usuario/form_usuario.html?erro=" + encode(mensagem);
    }

    /**
     * Redireciona com mensagem de sucesso (parâmetro ?sucesso= na URL).
     */
    private String redirectComMensagemSucesso(String mensagem) {
        return "redirect:/html/usuario/form_usuario.html?sucesso=" + encode(mensagem);
    }

    /**
     * Codifica a mensagem para URL.
     */
    private String encode(String mensagem) {
        try {
            return URLEncoder.encode(mensagem, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return mensagem;
        }
    }
}

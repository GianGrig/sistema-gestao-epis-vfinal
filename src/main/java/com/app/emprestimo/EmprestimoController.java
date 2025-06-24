package com.app.emprestimo;

import com.app.usuario.UsuarioRepository;
import com.app.epi.EpiRepository;
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
public class EmprestimoController {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EpiRepository epiRepository;

    @PostMapping("/emprestimos")
    public String salvar(@RequestParam int id_usuario,
                         @RequestParam int id_epi,
                         @RequestParam String data_retirada,
                         @RequestParam String data_prevista_devolucao,
                         @RequestParam boolean confirmacao_retirada) {

        // Verifica se o usuário existe
        if (usuarioRepository.buscarPorId(id_usuario) == null) {
            return redirectComErro("Usuário com ID " + id_usuario + " não encontrado.");
        }

        // Verifica se o EPI existe
        if (epiRepository.buscarPorId(id_epi) == null) {
            return redirectComErro("EPI com ID " + id_epi + " não encontrado.");
        }

        Emprestimo emprestimo = new Emprestimo(id_usuario, id_epi, data_retirada, data_prevista_devolucao, confirmacao_retirada);
        emprestimoRepository.salvar(emprestimo);
        return redirectComSucesso("Empréstimo cadastrado com sucesso.");
    }

    @GetMapping("/emprestimos")
    @ResponseBody
    public List<Emprestimo> listar() {
        return emprestimoRepository.buscarTodos();
    }

    @PostMapping("/emprestimos/atualizar")
    public String atualizar(@RequestParam int id_emprestimo,
                            @RequestParam int id_usuario,
                            @RequestParam int id_epi,
                            @RequestParam String data_retirada,
                            @RequestParam String data_prevista_devolucao,
                            @RequestParam boolean confirmacao_retirada) {

        Emprestimo existente = emprestimoRepository.buscarPorId(id_emprestimo);
        if (existente == null) {
            return redirectComErro("Empréstimo com ID " + id_emprestimo + " não encontrado.");
        }

        // Verifica se o usuário existe
        if (usuarioRepository.buscarPorId(id_usuario) == null) {
            return redirectComErro("Usuário com ID " + id_usuario + " não encontrado.");
        }

        // Verifica se o EPI existe
        if (epiRepository.buscarPorId(id_epi) == null) {
            return redirectComErro("EPI com ID " + id_epi + " não encontrado.");
        }

        Emprestimo emprestimo = new Emprestimo(id_emprestimo, id_usuario, id_epi, data_retirada, data_prevista_devolucao, confirmacao_retirada);
        emprestimoRepository.atualizar(emprestimo);
        return redirectComSucesso("Empréstimo atualizado com sucesso.");
    }

    @GetMapping("/emprestimos/{id}")
    @ResponseBody
    public Emprestimo buscarPorId(@PathVariable int id) {
        Emprestimo emprestimo = emprestimoRepository.buscarPorId(id);
        if (emprestimo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado");
        }
        return emprestimo;
    }


    // Utilitários para redirecionamento com mensagem
    private String redirectComErro(String msg) {
        return "redirect:/emprestimo/form_emprestimo.html?erro=" + encode(msg);
    }

    private String redirectComSucesso(String msg) {
        return "redirect:/emprestimo/form_emprestimo.html?sucesso=" + encode(msg);
    }

    private String encode(String msg) {
        try {
            return URLEncoder.encode(msg, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return msg;
        }
    }
}

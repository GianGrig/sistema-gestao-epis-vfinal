package com.app.epi;

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
 * Controlador responsável por gerenciar requisições relacionadas aos EPIs.
 */
@Controller
@RequestMapping("/epis")
public class EpiController {

    @Autowired
    private EpiRepository epiRepository;

    @PostMapping
    public String salvar(@RequestParam String nome, @RequestParam int quantidade) {

        if (nome.length() < 2 || nome.length() > 100) {
            return redirectComMensagemErro("O nome deve ter entre 2 e 100 caracteres.");
        }

        if (quantidade < 0) {
            return redirectComMensagemErro("A quantidade não pode ser negativa.");
        }

        nome = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();
        epiRepository.salvar(new Epi(nome, quantidade));
        return redirectComMensagemSucesso("EPI cadastrado com sucesso.");
    }

    @PostMapping("/atualizar")
    public String atualizar(@RequestParam int id_epi,
                            @RequestParam String nome,
                            @RequestParam int quantidade) {

        if (epiRepository.buscarPorId(id_epi) == null) {
            return redirectComMensagemErro("EPI com ID " + id_epi + " não encontrado.");
        }

        if (nome.length() < 2 || nome.length() > 100) {
            return redirectComMensagemErro("O nome deve ter entre 2 e 100 caracteres.");
        }

        if (quantidade < 0) {
            return redirectComMensagemErro("A quantidade não pode ser negativa.");
        }

        nome = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();
        epiRepository.atualizar(new Epi(id_epi, nome, quantidade));
        return redirectComMensagemSucesso("EPI atualizado com sucesso.");
    }

    @PostMapping("/deletar")
    public String deletar(@RequestParam int id_epi) {

        if (epiRepository.buscarPorId(id_epi) == null) {
            return redirectComMensagemErro("EPI com ID " + id_epi + " não encontrado.");
        }

        if (epiRepository.estaVinculadoAEmprestimo(id_epi)) {
            return redirectComMensagemErro("O EPI não pode ser deletado pois está vinculado a um empréstimo.");
        }

        epiRepository.deletar(id_epi);
        return redirectComMensagemSucesso("EPI deletado com sucesso.");
    }

    @GetMapping("/form")
    public String formHtml() {
        return "epi/form_epi";
    }

    @GetMapping("/listar")
    public String listarHtml() {
        return "epi/listar_epi";
    }

    @GetMapping("/todos")
    @ResponseBody
    public List<Epi> listar() {
        return epiRepository.buscarTodos();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Epi buscarPorId(@PathVariable int id) {
        Epi epi = epiRepository.buscarPorId(id);
        if (epi == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "EPI com ID " + id + " não encontrado.");
        }
        return epi;
    }

    private String redirectComMensagemErro(String mensagem) {
        return "redirect:/epis/form?erro=" + encode(mensagem);
    }

    private String redirectComMensagemSucesso(String mensagem) {
        return "redirect:/epis/form?sucesso=" + encode(mensagem);
    }

    private String encode(String mensagem) {
        try {
            return URLEncoder.encode(mensagem, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return mensagem;
        }
    }
}


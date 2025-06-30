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
public class EpiController {

    @Autowired
    private EpiRepository epiRepository;

    /**
     * Cadastra um novo EPI com validações.
     */
    @PostMapping("/html/epis")
    public String salvar(@RequestParam String nome, @RequestParam int quantidade) {

        // Validação: nome deve ter entre 2 e 100 caracteres
        if (nome.length() < 2 || nome.length() > 100) {
            return redirectComMensagemErro("O nome deve ter entre 2 e 100 caracteres.");
        }

        // Validação: quantidade não pode ser negativa
        if (quantidade < 0) {
            return redirectComMensagemErro("A quantidade não pode ser negativa.");
        }

        // Normaliza o nome (primeira letra maiúscula, restante minúsculo)
        nome = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();

        // Salva o EPI
        epiRepository.salvar(new Epi(nome, quantidade));
        return redirectComMensagemSucesso("EPI cadastrado com sucesso.");
    }

    /**
     * Lista todos os EPIs cadastrados (resposta em JSON).
     */
    @GetMapping("/html/epis")
    @ResponseBody
    public List<Epi> listar() {
        return epiRepository.buscarTodos();
    }

    /**
     * Atualiza os dados de um EPI existente.
     */
    @PostMapping("/html/epis/atualizar")
    public String atualizar(@RequestParam int id_epi,
                            @RequestParam String nome,
                            @RequestParam int quantidade) {

        // Verifica se o EPI existe
        Epi existente = epiRepository.buscarPorId(id_epi);
        if (existente == null) {
            return redirectComMensagemErro("EPI com ID " + id_epi + " não encontrado.");
        }

        // Validação: nome com tamanho adequado
        if (nome.length() < 2 || nome.length() > 100) {
            return redirectComMensagemErro("O nome deve ter entre 2 e 100 caracteres.");
        }

        // Validação: quantidade válida
        if (quantidade < 0) {
            return redirectComMensagemErro("A quantidade não pode ser negativa.");
        }

        // Formata o nome
        nome = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();

        // Atualiza o EPI no banco
        epiRepository.atualizar(new Epi(id_epi, nome, quantidade));
        return redirectComMensagemSucesso("EPI atualizado com sucesso.");
    }

    /**
     * Busca um EPI pelo ID.
     */
    @GetMapping("/html/epis/{id}")
    @ResponseBody
    public Epi buscarPorId(@PathVariable int id) {
        Epi epi = epiRepository.buscarPorId(id);
        if (epi == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "EPI com ID " + id + " não encontrado.");
        }
        return epi;
    }

    /**
     * Exclui um EPI, desde que não esteja vinculado a um empréstimo.
     */
    @PostMapping("/html/epis/deletar")
    public String deletar(@RequestParam int id_epi) {

        // Verifica se o EPI existe
        Epi existente = epiRepository.buscarPorId(id_epi);
        if (existente == null) {
            return redirectComMensagemErro("EPI com ID " + id_epi + " não encontrado.");
        }

        // Verifica se está vinculado a empréstimo
        if (epiRepository.estaVinculadoAEmprestimo(id_epi)) {
            return redirectComMensagemErro("O EPI não pode ser deletado pois está vinculado a um empréstimo.");
        }

        // Deleta o EPI
        epiRepository.deletar(id_epi);
        return redirectComMensagemSucesso("EPI deletado com sucesso.");
    }

    // Métodos utilitários

    /**
     * Redireciona com mensagem de erro.
     */
    private String redirectComMensagemErro(String mensagem) {
        return "redirect:/html/epi/form_epi.html?erro=" + encode(mensagem);
    }

    /**
     * Redireciona com mensagem de sucesso.
     */
    private String redirectComMensagemSucesso(String mensagem) {
        return "redirect:/html/epi/form_epi.html?sucesso=" + encode(mensagem);
    }

    /**
     * Codifica mensagem para URL.
     */
    private String encode(String mensagem) {
        try {
            return URLEncoder.encode(mensagem, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return mensagem;
        }
    }
}

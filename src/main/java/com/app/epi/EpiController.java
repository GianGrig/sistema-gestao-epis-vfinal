package com.app.epi;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class EpiController {

    @Autowired
    private EpiRepository epiRepository;

    @PostMapping("/epis")
    public String salvar(@RequestParam String nome, @RequestParam int quantidade) {
        if (nome.length() < 2 || nome.length() > 100) {
            return redirectComMensagemErro("O nome deve ter entre 2 e 100 caracteres.");
        }

        if (quantidade < 0) {
            return redirectComMensagemErro("A quantidade não pode ser negativa.");
        }

        // Normaliza o nome
        nome = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();

        epiRepository.salvar(new Epi(nome, quantidade));
        return redirectComMensagemSucesso("EPI cadastrado com sucesso.");
    }

    @GetMapping("/epis")
    @ResponseBody
    public List<Epi> listar() {
        return epiRepository.buscarTodos();
    }

    @PostMapping("/epis/atualizar")
    public String atualizar(@RequestParam int id_epi,
                            @RequestParam String nome,
                            @RequestParam int quantidade) {

        Epi existente = epiRepository.buscarPorId(id_epi);
        if (existente == null) {
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

    @PostMapping("/epis/deletar")
    public String deletar(@RequestParam int id_epi) {
        Epi existente = epiRepository.buscarPorId(id_epi);
        if (existente == null) {
            return redirectComMensagemErro("EPI com ID " + id_epi + " não encontrado.");
        }

        if (epiRepository.estaVinculadoAEmprestimo(id_epi)) {
            return redirectComMensagemErro("O EPI não pode ser deletado pois está vinculado a um empréstimo.");
        }

        epiRepository.deletar(id_epi);
        return redirectComMensagemSucesso("EPI deletado com sucesso.");
    }

    // Utilitários para mensagens
    private String redirectComMensagemErro(String mensagem) {
        return "redirect:/epi/form_epi.html?erro=" + encode(mensagem);
    }

    private String redirectComMensagemSucesso(String mensagem) {
        return "redirect:/epi/form_epi.html?sucesso=" + encode(mensagem);
    }

    private String encode(String mensagem) {
        try {
            return URLEncoder.encode(mensagem, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return mensagem;
        }
    }
}

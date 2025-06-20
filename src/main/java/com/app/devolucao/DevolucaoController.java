package com.app.devolucao;

import com.app.emprestimo.EmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class DevolucaoController {

    @Autowired
    private DevolucaoRepository devolucaoRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @PostMapping("/devolucao")
    public String salvar(@RequestParam int id_emprestimo,
                         @RequestParam String data_devolucao) {

        if (emprestimoRepository.buscarPorId(id_emprestimo) == null) {
            return redirectComMensagemErro("Empréstimo com ID " + id_emprestimo + " não encontrado.");
        }

        devolucaoRepository.salvar(new Devolucao(id_emprestimo, data_devolucao));
        return redirectComMensagemSucesso("Devolução registrada com sucesso.");
    }

    @GetMapping("/devolucao")
    @ResponseBody
    public List<Devolucao> listar() {
        return devolucaoRepository.buscarTodos();
    }

    @PostMapping("/devolucao/atualizar")
    public String atualizar(@RequestParam int id_devolucao,
                            @RequestParam int id_emprestimo,
                            @RequestParam String data_devolucao) {

        if (devolucaoRepository.buscarPorId(id_devolucao) == null) {
            return redirectComMensagemErro("Devolução com ID " + id_devolucao + " não encontrada.");
        }

        if (emprestimoRepository.buscarPorId(id_emprestimo) == null) {
            return redirectComMensagemErro("Empréstimo com ID " + id_emprestimo + " não encontrado.");
        }

        devolucaoRepository.atualizar(new Devolucao(id_devolucao, id_emprestimo, data_devolucao));
        return redirectComMensagemSucesso("Devolução atualizada com sucesso.");
    }

    // Métodos utilitários
    private String redirectComMensagemErro(String msg) {
        return "redirect:/devolucao/form_devolucao.html?erro=" + URLEncoder.encode(msg, StandardCharsets.UTF_8);
    }

    private String redirectComMensagemSucesso(String msg) {
        return "redirect:/devolucao/form_devolucao.html?sucesso=" + URLEncoder.encode(msg, StandardCharsets.UTF_8);
    }
}

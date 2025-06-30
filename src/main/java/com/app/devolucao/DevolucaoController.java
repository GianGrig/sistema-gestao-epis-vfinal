package com.app.devolucao;

import com.app.emprestimo.EmprestimoRepository;
import com.app.usuario.Perfil;
import com.app.usuario.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DevolucaoController {

    @Autowired
    private DevolucaoRepository devolucaoRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @PostMapping("/html/devolucao")
    public String salvar(@RequestParam int id_emprestimo,
                         @RequestParam String data_devolucao) {
        if (emprestimoRepository.buscarPorId(id_emprestimo) == null) {
            return redirectComMensagemErro("Empréstimo com ID " + id_emprestimo + " não encontrado.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime data = LocalDateTime.parse(data_devolucao, formatter);

        devolucaoRepository.salvar(new Devolucao(id_emprestimo, data));
        return redirectComMensagemSucesso("Devolução registrada com sucesso.");
    }

    @GetMapping("/html/devolucao")
    @ResponseBody
    public List<Devolucao> listar() {
        return devolucaoRepository.buscarTodos();
    }

    @PostMapping("/html/devolucao/atualizar")
    public String atualizar(@RequestParam int id_devolucao,
                            @RequestParam int id_emprestimo,
                            @RequestParam String data_devolucao) {

        if (devolucaoRepository.buscarPorId(id_devolucao) == null) {
            return redirectComMensagemErro("Devolução com ID " + id_devolucao + " não encontrada.");
        }

        if (emprestimoRepository.buscarPorId(id_emprestimo) == null) {
            return redirectComMensagemErro("Empréstimo com ID " + id_emprestimo + " não encontrado.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime data = LocalDateTime.parse(data_devolucao, formatter);

        devolucaoRepository.atualizar(new Devolucao(id_devolucao, id_emprestimo, data));
        return redirectComMensagemSucesso("Devolução atualizada com sucesso.");
    }

    @GetMapping("/html/devolucao/{id:[0-9]+}")
    @ResponseBody
    public Devolucao buscarPorId(@PathVariable int id) {
        Devolucao devolucao = devolucaoRepository.buscarPorId(id);
        if (devolucao == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Devolução não encontrada");
        }
        return devolucao;
    }

    // Métodos utilitários
    private String redirectComMensagemErro(String msg) {
        return "redirect:/html/devolucao/form_devolucao.html?erro=" + URLEncoder.encode(msg, StandardCharsets.UTF_8);
    }

    private String redirectComMensagemSucesso(String msg) {
        return "redirect:/html/devolucao/form_devolucao.html?sucesso=" + URLEncoder.encode(msg, StandardCharsets.UTF_8);
    }

    @GetMapping("/html/devolucoes/colaborador")
    @ResponseBody
    public List<DevolucaoDTO> listarDevolucoesColaborador(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario != null && usuario.getPerfil() == Perfil.COLABORADOR) {
            return devolucaoRepository.buscarDTOsPorUsuario(usuario.getId_usuario());
        }
        return new ArrayList<>();
    }
}

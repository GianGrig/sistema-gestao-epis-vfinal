package com.app.devolucao;

import com.app.emprestimo.EmprestimoRepository;
import com.app.security.UsuarioDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/devolucao")
public class DevolucaoController {

    @Autowired
    private DevolucaoRepository devolucaoRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @PostMapping
    public String salvar(@RequestParam int id_emprestimo,
                         @RequestParam String data_devolucao) {

        if (emprestimoRepository.buscarPorId(id_emprestimo) == null) {
            return redirectComMensagemErro("Empréstimo com ID " + id_emprestimo + " não encontrado.");
        }

        LocalDateTime data = LocalDateTime.parse(data_devolucao, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        devolucaoRepository.salvar(new Devolucao(id_emprestimo, data));
        return redirectComMensagemSucesso("Devolução registrada com sucesso.");
    }

    @PostMapping("/atualizar")
    public String atualizar(@RequestParam int id_devolucao,
                            @RequestParam int id_emprestimo,
                            @RequestParam String data_devolucao) {

        if (devolucaoRepository.buscarPorId(id_devolucao) == null) {
            return redirectComMensagemErro("Devolução com ID " + id_devolucao + " não encontrada.");
        }

        if (emprestimoRepository.buscarPorId(id_emprestimo) == null) {
            return redirectComMensagemErro("Empréstimo com ID " + id_emprestimo + " não encontrado.");
        }

        LocalDateTime data = LocalDateTime.parse(data_devolucao, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        devolucaoRepository.atualizar(new Devolucao(id_devolucao, id_emprestimo, data));
        return redirectComMensagemSucesso("Devolução atualizada com sucesso.");
    }

    @GetMapping("/form")
    public String formHtml() {
        return "devolucao/form_devolucao";
    }

    @GetMapping("/listar")
    public String listarHtml() {
        return "devolucao/listar_devolucao";
    }

    @GetMapping("/listar/colaborador")
    public String listarColaboradorHtml() {
        return "devolucao/listar_devolucao_colaborador";
    }

    @GetMapping("/todos")
    @ResponseBody
    public List<Devolucao> listar() {
        return devolucaoRepository.buscarTodos();
    }

    @GetMapping("/{id:[0-9]+}")
    @ResponseBody
    public Devolucao buscarPorId(@PathVariable int id) {
        Devolucao devolucao = devolucaoRepository.buscarPorId(id);
        if (devolucao == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Devolução não encontrada");
        }
        return devolucao;
    }

    @GetMapping("/colaborador")
    @ResponseBody
    public List<DevolucaoDTO> listarDevolucoesColaborador() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof UsuarioDetails usuarioDetails) {
            int idUsuario = usuarioDetails.getId();
            return devolucaoRepository.buscarDTOsPorUsuario(idUsuario);
        }

        return new ArrayList<>();
    }

    private String redirectComMensagemErro(String msg) {
        return "redirect:/devolucao/form?erro=" + URLEncoder.encode(msg, StandardCharsets.UTF_8);
    }

    private String redirectComMensagemSucesso(String msg) {
        return "redirect:/devolucao/form?sucesso=" + URLEncoder.encode(msg, StandardCharsets.UTF_8);
    }
}

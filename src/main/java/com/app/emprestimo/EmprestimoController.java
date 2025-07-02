package com.app.emprestimo;

import com.app.security.UsuarioDetails;
import com.app.usuario.Perfil;
import com.app.usuario.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador responsável pelas operações de empréstimo de EPIs.
 */
@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoRepository emprestimoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EpiRepository epiRepository;

    @PostMapping
    public String salvar(@RequestParam int id_usuario,
                         @RequestParam int id_epi,
                         @RequestParam String data_retirada,
                         @RequestParam String data_prevista_devolucao,
                         @RequestParam boolean confirmacao_retirada) {

        if (usuarioRepository.buscarPorId(id_usuario) == null) {
            return redirectComErro("Usuário com ID " + id_usuario + " não encontrado.");
        }

        if (epiRepository.buscarPorId(id_epi) == null) {
            return redirectComErro("EPI com ID " + id_epi + " não encontrado.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime retirada = LocalDateTime.parse(data_retirada, formatter);
        LocalDateTime prevista = LocalDateTime.parse(data_prevista_devolucao, formatter);

        emprestimoRepository.salvar(new Emprestimo(id_usuario, id_epi, retirada, prevista, confirmacao_retirada));
        return redirectComSucesso("Empréstimo cadastrado com sucesso.");
    }

    @PostMapping("/atualizar")
    public String atualizar(@RequestParam int id_emprestimo,
                            @RequestParam int id_usuario,
                            @RequestParam int id_epi,
                            @RequestParam String data_retirada,
                            @RequestParam String data_prevista_devolucao,
                            @RequestParam boolean confirmacao_retirada) {

        if (emprestimoRepository.buscarPorId(id_emprestimo) == null) {
            return redirectComErro("Empréstimo com ID " + id_emprestimo + " não encontrado.");
        }

        if (usuarioRepository.buscarPorId(id_usuario) == null) {
            return redirectComErro("Usuário com ID " + id_usuario + " não encontrado.");
        }

        if (epiRepository.buscarPorId(id_epi) == null) {
            return redirectComErro("EPI com ID " + id_epi + " não encontrado.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime retirada = LocalDateTime.parse(data_retirada, formatter);
        LocalDateTime prevista = LocalDateTime.parse(data_prevista_devolucao, formatter);

        emprestimoRepository.atualizar(new Emprestimo(id_emprestimo, id_usuario, id_epi, retirada, prevista, confirmacao_retirada));
        return redirectComSucesso("Empréstimo atualizado com sucesso.");
    }

    @GetMapping("/form")
    public String exibirFormulario() {
        return "emprestimo/form_emprestimo";
    }

    @GetMapping("/listar")
    public String listarHtml() {
        return "emprestimo/listar_emprestimo";
    }

    @GetMapping("/listar/colaborador")
    public String listarColaboradorHtml() {
        return "emprestimo/listar_emprestimo_colaborador";
    }

    @GetMapping("/todos")
    @ResponseBody
    public List<Emprestimo> listar() {
        return emprestimoRepository.buscarTodos();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Emprestimo buscarPorId(@PathVariable int id) {
        Emprestimo emprestimo = emprestimoRepository.buscarPorId(id);
        if (emprestimo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado");
        }
        return emprestimo;
    }

    @GetMapping("/colaborador")
    @ResponseBody
    public List<EmprestimoDTO> listarEmprestimosColaborador() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof UsuarioDetails usuarioDetails) {
            int idUsuario = usuarioDetails.getId();
            return emprestimoRepository.buscarDTOsPorUsuario(idUsuario);
        }

        return new ArrayList<>();
    }

    private String redirectComErro(String msg) {
        return "redirect:/emprestimos/form?erro=" + encode(msg);
    }

    private String redirectComSucesso(String msg) {
        return "redirect:/emprestimos/form?sucesso=" + encode(msg);
    }

    private String encode(String msg) {
        try {
            return URLEncoder.encode(msg, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return msg;
        }
    }
}

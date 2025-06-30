package com.app.emprestimo;

import com.app.usuario.Perfil;
import com.app.usuario.Usuario;
import com.app.usuario.UsuarioRepository;
import com.app.epi.EpiRepository;
import jakarta.servlet.http.HttpSession;
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
public class EmprestimoController {

    @Autowired
    private EmprestimoRepository emprestimoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EpiRepository epiRepository;

    @PostMapping("/html/emprestimos")
    public String salvar(@RequestParam int id_usuario,
                         @RequestParam int id_epi,
                         @RequestParam String data_retirada,
                         @RequestParam String data_prevista_devolucao,
                         @RequestParam boolean confirmacao_retirada) {

        // Validação: usuário deve existir
        if (usuarioRepository.buscarPorId(id_usuario) == null) {
            return redirectComErro("Usuário com ID " + id_usuario + " não encontrado.");
        }

        // Validação: EPI deve existir
        if (epiRepository.buscarPorId(id_epi) == null) {
            return redirectComErro("EPI com ID " + id_epi + " não encontrado.");
        }

        // Converte as datas recebidas como String para LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime retirada = LocalDateTime.parse(data_retirada, formatter);
        LocalDateTime prevista = LocalDateTime.parse(data_prevista_devolucao, formatter);

        Emprestimo emprestimo = new Emprestimo(id_usuario, id_epi, retirada, prevista, confirmacao_retirada);
        emprestimoRepository.salvar(emprestimo);
        return redirectComSucesso("Empréstimo cadastrado com sucesso.");
    }

    @GetMapping("/html/emprestimos")
    @ResponseBody
    public List<Emprestimo> listar() {
        return emprestimoRepository.buscarTodos();
    }

    @PostMapping("/html/emprestimos/atualizar")
    public String atualizar(@RequestParam int id_emprestimo,
                            @RequestParam int id_usuario,
                            @RequestParam int id_epi,
                            @RequestParam String data_retirada,
                            @RequestParam String data_prevista_devolucao,
                            @RequestParam boolean confirmacao_retirada) {

        // Verifica se o empréstimo existe
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

        // Converte as strings de data para LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime retirada = LocalDateTime.parse(data_retirada, formatter);
        LocalDateTime prevista = LocalDateTime.parse(data_prevista_devolucao, formatter);

        Emprestimo emprestimo = new Emprestimo(id_emprestimo, id_usuario, id_epi, retirada, prevista, confirmacao_retirada);
        emprestimoRepository.atualizar(emprestimo);
        return redirectComSucesso("Empréstimo atualizado com sucesso.");
    }

    @GetMapping("/html/emprestimos/{id}")
    @ResponseBody
    public Emprestimo buscarPorId(@PathVariable int id) {
        Emprestimo emprestimo = emprestimoRepository.buscarPorId(id);
        if (emprestimo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado");
        }
        return emprestimo;
    }

    // Métodos de redirecionamento com mensagens

    private String redirectComErro(String msg) {
        return "redirect:/html/emprestimo/form_emprestimo.html?erro=" + encode(msg);
    }

    private String redirectComSucesso(String msg) {
        return "redirect:/html/emprestimo/form_emprestimo.html?sucesso=" + encode(msg);
    }

    private String encode(String msg) {
        try {
            return URLEncoder.encode(msg, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return msg;
        }
    }

    /**
     * Retorna os empréstimos associados ao colaborador logado (via sessão).
     */
    @GetMapping("/html/emprestimos/colaborador")
    @ResponseBody
    public List<EmprestimoDTO> listarEmprestimosColaborador(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario != null && usuario.getPerfil() == Perfil.COLABORADOR) {
            return emprestimoRepository.buscarDTOsPorUsuario(usuario.getId_usuario());
        }
        return new ArrayList<>();
    }
}

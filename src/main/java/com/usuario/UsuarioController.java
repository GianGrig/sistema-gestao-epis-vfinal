package com.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/usuario")
    public String salvar(@RequestParam String nome, @RequestParam String email, @RequestParam String senha, @RequestParam String perfil){
        usuarioRepository.salvar(new Usuario(nome, email, senha, perfil));
        return "redirect:/colaborador/listar_usuario.html";
    }

    @GetMapping("/usuario")
    @ResponseBody
    public List<Usuario> listar() {
        return usuarioRepository.buscarTodos();
    }

    @PostMapping("/usuario/atualizar")
    public String atualizar(@RequestParam int id_usuario, @RequestParam String nome, @RequestParam String email, @RequestParam String senha, @RequestParam String perfil) {
        usuarioRepository.atualizar(new Usuario(id_usuario, nome, email, senha, perfil));
        return "redirect:/colaborador/listar_usuario.html";
    }

    @PostMapping("/usuario/deletar")
    public String deletar(@RequestParam int id_usuario) {
        usuarioRepository.deletar(id_usuario);
        return "redirect:/colaborador/listar_usuario.html";
    }
}

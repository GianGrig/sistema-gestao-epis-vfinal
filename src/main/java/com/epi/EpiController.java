package com.epi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class EpiController {

    @Autowired
    private EpiRepository epiRepository;

    @PostMapping("/epis")
    public String salvar(@RequestParam String nome, @RequestParam int quantidade) {
        epiRepository.salvar(new Epi(nome, quantidade));
        return "redirect:/epi/listar_epi.html";
    }

    @GetMapping("/epis")
    @ResponseBody
    public List<Epi> listar() {
        return epiRepository.buscarTodos();
    }

    @PostMapping("/epis/atualizar")
    public String atualizar(@RequestParam int id_epi, @RequestParam String nome, @RequestParam int quantidade) {
        epiRepository.atualizar(new Epi(id_epi, nome, quantidade));
        return "redirect:/epi/listar_epi.html";
    }

    @PostMapping("/epis/deletar")
    public String deletar(@RequestParam int id_epi) {
        epiRepository.deletar(id_epi);
        return "redirect:/epi/listar_epi.html";
    }
}

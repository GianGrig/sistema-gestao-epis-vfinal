package com.app.emprestimo;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class EmprestimoRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public void salvar(Emprestimo e) {
        String sql = "INSERT INTO emprestimo (id_usuario, id_epi, data_retirada, data_prevista_devolucao, confirmacao_retirada) VALUES (?, ?, ?, ?, ?)";
        jdbc.update(sql, e.getId_usuario(), e.getId_epi(), e.getData_retirada(), e.getData_prevista_devolucao(), e.isConfirmacao_retirada());
    }

    public List<Emprestimo> buscarTodos() {
        return jdbc.query("SELECT * FROM emprestimo", (rs, rowNum) -> new Emprestimo(
                rs.getInt("id_emprestimo"),
                rs.getInt("id_usuario"),
                rs.getInt("id_epi"),
                rs.getString("data_retirada"),
                rs.getString("data_prevista_devolucao"),
                rs.getBoolean("confirmacao_retirada")
        ));
    }

    public void atualizar(Emprestimo e) {
        String sql = "UPDATE emprestimo SET id_usuario = ?, id_epi = ?, data_retirada = ?, data_prevista_devolucao = ?, confirmacao_retirada = ? WHERE id_emprestimo = ?";
        jdbc.update(sql, e.getId_usuario(), e.getId_epi(), e.getData_retirada(), e.getData_prevista_devolucao(), e.isConfirmacao_retirada(), e.getId_emprestimo());
    }

    public Emprestimo buscarPorId(int id) {
        String sql = "SELECT * FROM emprestimo WHERE id_emprestimo = ?";
        List<Emprestimo> resultado = jdbc.query(sql, new Object[]{id}, (rs, rowNum) -> new Emprestimo(
                rs.getInt("id_emprestimo"),
                rs.getInt("id_usuario"),
                rs.getInt("id_epi"),
                rs.getString("data_retirada"),
                rs.getString("data_prevista_devolucao"),
                rs.getBoolean("confirmacao_retirada")
        ));
        return resultado.isEmpty() ? null : resultado.get(0);
    }

}

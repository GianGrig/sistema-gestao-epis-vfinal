package com.app.devolucao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class DevolucaoRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public void salvar(Devolucao d) {
        String sql = "INSERT INTO devolucao (id_emprestimo, data_devolucao) VALUES (?, ?)";
        jdbc.update(sql, d.getId_emprestimo(), d.getData_devolucao());
    }

    public List<Devolucao> buscarTodos() {
        return jdbc.query("SELECT * FROM devolucao", new RowMapper<Devolucao>() {
            @Override
            public Devolucao mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Devolucao(rs.getInt("id_devolucao"), rs.getInt("id_emprestimo"), rs.getString("data_devolucao"));
            }
        });
    }

    public void atualizar(Devolucao d) {
        String sql = "UPDATE devolucao SET id_emprestimo = ?, data_devolucao = ? WHERE id_devolucao = ?";
        jdbc.update(sql, d.getId_emprestimo(), d.getData_devolucao(), d.getId_devolucao());
    }

    public Devolucao buscarPorId(int id) {
        String sql = "SELECT * FROM devolucao WHERE id_devolucao = ?";
        List<Devolucao> resultado = jdbc.query(sql, new Object[]{id}, (rs, rowNum) ->
                new Devolucao(rs.getInt("id_devolucao"), rs.getInt("id_emprestimo"), rs.getString("data_devolucao"))
        );
        return resultado.isEmpty() ? null : resultado.get(0);
    }
}

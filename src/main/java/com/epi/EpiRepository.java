package com.epi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class EpiRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public void salvar(Epi epi) {
        String sql = "INSERT INTO epis (nome, quantidade) VALUES (?, ?)";
        jdbc.update(sql, epi.getNome(), epi.getQuantidade());
    }

    public List<Epi> buscarTodos() {
        return jdbc.query("SELECT * FROM epis", new RowMapper<Epi>() {
            @Override
            public Epi mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Epi(rs.getInt("id"), rs.getString("nome"), rs.getInt("quantidade"));
            }
        });
    }

    public void atualizar(Epi epi) {
        String sql = "UPDATE epis SET nome = ?, quantidade = ? WHERE id = ?";
        jdbc.update(sql, epi.getNome(), epi.getQuantidade(), epi.getId_epi());
    }

    public void deletar(int id_epi) {
        String sql = "DELETE FROM epis WHERE id = ?";
        jdbc.update(sql, id_epi);
    }
}

package com.app.emprestimo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class EmprestimoRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public void salvar(Emprestimo e) {
        String sql = "INSERT INTO emprestimo (id_usuario, id_epi, data_retirada, data_prevista_devolucao, confirmacao_retirada) VALUES (?, ?, ?, ?, ?)";
        jdbc.update(sql,
                e.getId_usuario(),
                e.getId_epi(),
                Timestamp.valueOf(e.getData_retirada()),
                Timestamp.valueOf(e.getData_prevista_devolucao()),
                e.isConfirmacao_retirada()
        );
    }

    public List<Emprestimo> buscarTodos() {
        String sql = "SELECT * FROM emprestimo";
        return jdbc.query(sql, new RowMapper<Emprestimo>() {
            @Override
            public Emprestimo mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Emprestimo(
                        rs.getInt("id_emprestimo"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_epi"),
                        rs.getTimestamp("data_retirada").toLocalDateTime(),
                        rs.getTimestamp("data_prevista_devolucao").toLocalDateTime(),
                        rs.getBoolean("confirmacao_retirada")
                );
            }
        });
    }

    public void atualizar(Emprestimo e) {
        String sql = "UPDATE emprestimo SET id_usuario = ?, id_epi = ?, data_retirada = ?, data_prevista_devolucao = ?, confirmacao_retirada = ? WHERE id_emprestimo = ?";
        jdbc.update(sql,
                e.getId_usuario(),
                e.getId_epi(),
                Timestamp.valueOf(e.getData_retirada()),
                Timestamp.valueOf(e.getData_prevista_devolucao()),
                e.isConfirmacao_retirada(),
                e.getId_emprestimo()
        );
    }

    public Emprestimo buscarPorId(int id) {
        String sql = "SELECT * FROM emprestimo WHERE id_emprestimo = ?";
        List<Emprestimo> resultado = jdbc.query(sql, new Object[]{id}, new RowMapper<Emprestimo>() {
            @Override
            public Emprestimo mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Emprestimo(
                        rs.getInt("id_emprestimo"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_epi"),
                        rs.getTimestamp("data_retirada").toLocalDateTime(),
                        rs.getTimestamp("data_prevista_devolucao").toLocalDateTime(),
                        rs.getBoolean("confirmacao_retirada")
                );
            }
        });
        return resultado.isEmpty() ? null : resultado.get(0);
    }
}

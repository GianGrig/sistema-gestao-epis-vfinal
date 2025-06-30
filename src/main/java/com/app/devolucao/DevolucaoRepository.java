package com.app.devolucao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Repositório responsável pelas operações de banco de dados relacionadas à devolução.
 */
@Repository
public class DevolucaoRepository {

    @Autowired
    private JdbcTemplate jdbc;

    /**
     * Salva uma nova devolução no banco.
     */
    public void salvar(Devolucao d) {
        String sql = "INSERT INTO devolucao (id_emprestimo, data_devolucao) VALUES (?, ?)";
        jdbc.update(sql, d.getId_emprestimo(), Timestamp.valueOf(d.getData_devolucao()));
    }

    /**
     * Retorna todas as devoluções registradas.
     */
    public List<Devolucao> buscarTodos() {
        return jdbc.query("SELECT * FROM devolucao", (rs, rowNum) -> new Devolucao(
                rs.getInt("id_devolucao"),
                rs.getInt("id_emprestimo"),
                rs.getTimestamp("data_devolucao").toLocalDateTime()
        ));
    }

    /**
     * Atualiza uma devolução existente.
     */
    public void atualizar(Devolucao d) {
        String sql = "UPDATE devolucao SET id_emprestimo = ?, data_devolucao = ? WHERE id_devolucao = ?";
        jdbc.update(sql, d.getId_emprestimo(), Timestamp.valueOf(d.getData_devolucao()), d.getId_devolucao());
    }

    /**
     * Busca uma devolução pelo ID.
     */
    public Devolucao buscarPorId(int id) {
        String sql = "SELECT * FROM devolucao WHERE id_devolucao = ?";
        List<Devolucao> resultado = jdbc.query(sql, new Object[]{id}, (rs, rowNum) -> new Devolucao(
                rs.getInt("id_devolucao"),
                rs.getInt("id_emprestimo"),
                rs.getTimestamp("data_devolucao").toLocalDateTime()
        ));
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    /**
     * Busca devoluções feitas por um usuário específico.
     */
    public List<Devolucao> buscarPorUsuario(int idUsuario) {
        String sql = """
                    SELECT d.id_devolucao, d.id_emprestimo, d.data_devolucao
                    FROM devolucao d
                    JOIN emprestimo e ON d.id_emprestimo = e.id_emprestimo
                    WHERE e.id_usuario = ?
                """;

        return jdbc.query(sql, new Object[]{idUsuario}, (rs, rowNum) -> new Devolucao(
                rs.getInt("id_devolucao"),
                rs.getInt("id_emprestimo"),
                rs.getTimestamp("data_devolucao").toLocalDateTime()
        ));
    }

    /**
     * Busca DTOs de devoluções de um usuário (nome do EPI + datas).
     */
    public List<DevolucaoDTO> buscarDTOsPorUsuario(int idUsuario) {
        String sql = """
                    SELECT ep.nome AS nome_epi, em.data_retirada, d.data_devolucao
                    FROM devolucao d
                    JOIN emprestimo em ON d.id_emprestimo = em.id_emprestimo
                    JOIN epis ep ON em.id_epi = ep.id_epi
                    WHERE em.id_usuario = ?
                """;

        return jdbc.query(sql, new Object[]{idUsuario}, (rs, rowNum) -> new DevolucaoDTO(
                rs.getString("nome_epi"),
                rs.getTimestamp("data_retirada").toLocalDateTime(),
                rs.getTimestamp("data_devolucao").toLocalDateTime()
        ));
    }
}

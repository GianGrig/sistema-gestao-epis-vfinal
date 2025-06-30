package com.app.emprestimo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Classe responsável por executar as operações no banco de dados relacionadas ao empréstimo.
 */
@Repository
public class EmprestimoRepository {

    @Autowired
    private JdbcTemplate jdbc;

    /**
     * Insere um novo empréstimo no banco.
     */
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

    /**
     * Retorna todos os empréstimos cadastrados.
     */
    public List<Emprestimo> buscarTodos() {
        String sql = "SELECT * FROM emprestimo";
        return jdbc.query(sql, (rs, rowNum) -> new Emprestimo(
                rs.getInt("id_emprestimo"),
                rs.getInt("id_usuario"),
                rs.getInt("id_epi"),
                rs.getTimestamp("data_retirada").toLocalDateTime(),
                rs.getTimestamp("data_prevista_devolucao").toLocalDateTime(),
                rs.getBoolean("confirmacao_retirada")
        ));
    }

    /**
     * Atualiza um empréstimo existente.
     */
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

    /**
     * Busca um empréstimo pelo ID.
     */
    public Emprestimo buscarPorId(int id) {
        String sql = "SELECT * FROM emprestimo WHERE id_emprestimo = ?";
        List<Emprestimo> resultado = jdbc.query(sql, new Object[]{id}, (rs, rowNum) -> new Emprestimo(
                rs.getInt("id_emprestimo"),
                rs.getInt("id_usuario"),
                rs.getInt("id_epi"),
                rs.getTimestamp("data_retirada").toLocalDateTime(),
                rs.getTimestamp("data_prevista_devolucao").toLocalDateTime(),
                rs.getBoolean("confirmacao_retirada")
        ));
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    /**
     * Busca empréstimos por ID de usuário.
     */
    public List<Emprestimo> buscarPorUsuario(int idUsuario) {
        String sql = "SELECT * FROM emprestimo WHERE id_usuario = ?";
        return jdbc.query(sql, new Object[]{idUsuario}, (rs, rowNum) -> new Emprestimo(
                rs.getInt("id_emprestimo"),
                rs.getInt("id_usuario"),
                rs.getInt("id_epi"),
                rs.getTimestamp("data_retirada").toLocalDateTime(),
                rs.getTimestamp("data_prevista_devolucao").toLocalDateTime(),
                rs.getBoolean("confirmacao_retirada")
        ));
    }

    /**
     * Retorna empréstimos formatados para visualização por colaboradores (DTO).
     */
    public List<EmprestimoDTO> buscarDTOsPorUsuario(int idUsuario) {
        String sql = """
                SELECT ep.nome AS nome_epi, em.data_retirada, em.data_prevista_devolucao, em.confirmacao_retirada
                FROM emprestimo em
                JOIN epis ep ON em.id_epi = ep.id_epi
                WHERE em.id_usuario = ?
                """;

        return jdbc.query(sql, new Object[]{idUsuario}, (rs, rowNum) -> new EmprestimoDTO(
                rs.getString("nome_epi"),
                rs.getTimestamp("data_retirada").toLocalDateTime(),
                rs.getTimestamp("data_prevista_devolucao").toLocalDateTime(),
                rs.getBoolean("confirmacao_retirada")
        ));
    }
}

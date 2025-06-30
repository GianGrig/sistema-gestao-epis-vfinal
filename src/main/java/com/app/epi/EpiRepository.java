package com.app.epi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Repositório responsável pelas operações com a tabela 'epis' no banco de dados.
 */
@Repository
public class EpiRepository {

    @Autowired
    private JdbcTemplate jdbc;

    /**
     * Insere um novo EPI no banco.
     */
    public void salvar(Epi epi) {
        String sql = "INSERT INTO epis (nome, quantidade) VALUES (?, ?)";
        jdbc.update(sql, epi.getNome(), epi.getQuantidade());
    }

    /**
     * Busca todos os EPIs cadastrados.
     */
    public List<Epi> buscarTodos() {
        return jdbc.query("SELECT * FROM epis", new RowMapper<Epi>() {
            @Override
            public Epi mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Epi(rs.getInt("id_epi"), rs.getString("nome"), rs.getInt("quantidade"));
            }
        });
    }

    /**
     * Atualiza os dados de um EPI.
     */
    public void atualizar(Epi epi) {
        String sql = "UPDATE epis SET nome = ?, quantidade = ? WHERE id_epi = ?";
        jdbc.update(sql, epi.getNome(), epi.getQuantidade(), epi.getId_epi());
    }

    /**
     * Deleta um EPI pelo ID.
     */
    public void deletar(int id_epi) {
        String sql = "DELETE FROM epis WHERE id_epi = ?";
        jdbc.update(sql, id_epi);
    }

    /**
     * Busca um EPI pelo ID.
     */
    public Epi buscarPorId(int id_epi) {
        String sql = "SELECT * FROM epis WHERE id_epi = ?";
        List<Epi> lista = jdbc.query(sql, (rs, rowNum) -> new Epi(
                rs.getInt("id_epi"),
                rs.getString("nome"),
                rs.getInt("quantidade")
        ), id_epi);

        return lista.isEmpty() ? null : lista.get(0);
    }

    /**
     * Verifica se o EPI está vinculado a algum empréstimo.
     */
    public boolean estaVinculadoAEmprestimo(int id_epi) {
        String sql = "SELECT COUNT(*) FROM emprestimo WHERE id_epi = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, id_epi);
        return count != null && count > 0;
    }
}

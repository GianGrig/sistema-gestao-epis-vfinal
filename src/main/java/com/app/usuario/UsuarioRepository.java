package com.app.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Classe responsável pelas operações com o banco de dados relacionadas ao usuário.
 */
@Repository
public class UsuarioRepository {

    @Autowired
    private JdbcTemplate jdbc;

    /**
     * Salva um novo usuário no banco.
     */
    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, email, senha, perfil) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getPerfil().toString());
    }

    /**
     * Retorna a lista de todos os usuários cadastrados.
     */
    public List<Usuario> buscarTodos() {
        return jdbc.query("SELECT * FROM usuarios", new RowMapper<Usuario>() {
            @Override
            public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        Perfil.valueOf(rs.getString("perfil"))
                );
            }
        });
    }

    /**
     * Busca um usuário pelo ID.
     */
    public Usuario buscarPorId(int id_usuario) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        List<Usuario> lista = jdbc.query(sql, (rs, rowNum) -> new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("senha"),
                Perfil.valueOf(rs.getString("perfil"))
        ), id_usuario);
        return lista.isEmpty() ? null : lista.get(0);
    }

    /**
     * Verifica se o usuário está vinculado a algum empréstimo.
     */
    public boolean estaVinculadoAEmprestimo(int id_usuario) {
        String sql = "SELECT COUNT(*) FROM emprestimo WHERE id_usuario = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, id_usuario);
        return count != null && count > 0;
    }

    /**
     * Atualiza os dados de um usuário.
     */
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ?, perfil = ? WHERE id_usuario = ?";
        jdbc.update(sql, usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getPerfil().toString(), usuario.getId_usuario());
    }

    /**
     * Remove um usuário do banco.
     */
    public void deletar(int id_usuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        jdbc.update(sql, id_usuario);
    }

    /**
     * Busca um usuário pelo email.
     */
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        List<Usuario> lista = jdbc.query(sql, (rs, rowNum) -> new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("senha"),
                Perfil.valueOf(rs.getString("perfil"))
        ), email);
        return lista.isEmpty() ? null : lista.get(0);
    }
}

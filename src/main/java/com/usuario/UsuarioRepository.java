package com.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UsuarioRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public void salvar(Usuario usuario){
        String sql = "INSERT INTO usuario (nome, email, senha, perfil) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getPerfil());
    }

    public List<Usuario> buscarTodos() {
        return jdbc.query("SELECT * FROM usuario", new RowMapper<Usuario>() {
            @Override
            public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Usuario(rs.getInt("id_usuario"), rs.getString("nome"), rs.getString("email"), rs.getString("senha"), rs.getString("perfil"));
            }
        });
    }

    public void atualizar(Usuario usuario){
        String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, perfil = ? WHERE id_usuario = ?";
        jdbc.update(sql, usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getPerfil());
    }

    public void deletar(int id_usuario) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        jdbc.update(sql, id_usuario);
    }
}

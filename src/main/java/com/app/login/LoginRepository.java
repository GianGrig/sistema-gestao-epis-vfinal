package com.app.login;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoginRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean autenticar(Login login) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ? AND senha = ? AND perfil = ?";
        Integer count = jdbcTemplate.queryForObject(
                sql,
                new Object[]{ login.getEmail(), login.getSenha(), login.getPerfil().name() },
                Integer.class
        );
        return count != null && count > 0;
    }
}

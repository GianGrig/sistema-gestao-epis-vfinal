package com.app.login;

import com.app.usuario.Perfil;

/**
 * Classe que representa os dados de login fornecidos pelo usuário.
 */
public class Login {
    private String email;
    private String senha;
    private Perfil perfil;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}

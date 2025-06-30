package com.app.login;

import com.app.usuario.Perfil;

public class Login {
    private String email;
    private String senha;
    private Perfil perfil;

    // Getters e Setters
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

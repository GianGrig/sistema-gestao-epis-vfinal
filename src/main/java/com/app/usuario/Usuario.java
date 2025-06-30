package com.app.usuario;

/**
 * Classe que representa um usuário do sistema.
 */
public class Usuario {
    private int id_usuario;
    private String nome;
    private String email;
    private String senha;
    private Perfil perfil;

    /**
     * Construtor vazio.
     */
    public Usuario() {
    }

    /**
     * Construtor usado para criar um novo usuário sem ID.
     */
    public Usuario(String nome, String email, String senha, Perfil perfil) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
    }

    /**
     * Construtor usado quando o usuário já possui um ID (ex: vindo do banco).
     */
    public Usuario(int id_usuario, String nome, String email, String senha, Perfil perfil) {
        this.id_usuario = id_usuario;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
    }

    // Getters e setters

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

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

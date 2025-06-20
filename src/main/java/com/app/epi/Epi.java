package com.app.epi;

public class Epi {
    private int id_epi;
    private String nome;
    private int quantidade;

    public Epi() {
    }

    public Epi(String nome, int quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
    }

    public Epi(int id_epi, String nome, int quantidade) {
        this.id_epi = id_epi;
        this.nome = nome;
        this.quantidade = quantidade;
    }

    public int getId_epi() {
        return id_epi;
    }

    public void setId_epi(int id_epi) {
        this.id_epi = id_epi;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}

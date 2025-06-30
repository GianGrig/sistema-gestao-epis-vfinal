package com.app.epi;

/**
 * Classe que representa um Equipamento de Proteção Individual (EPI).
 */
public class Epi {
    private int id_epi;
    private String nome;
    private int quantidade;

    /**
     * Construtor vazio para frameworks.
     */
    public Epi() {
    }

    /**
     * Construtor para criação de EPI sem ID.
     */
    public Epi(String nome, int quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
    }

    /**
     * Construtor com todos os atributos, incluindo ID.
     */
    public Epi(int id_epi, String nome, int quantidade) {
        this.id_epi = id_epi;
        this.nome = nome;
        this.quantidade = quantidade;
    }

    // Getters e setters

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

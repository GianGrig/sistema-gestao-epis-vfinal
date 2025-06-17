package com.emprestimo;

public class Emprestimo {
    private int id_emprestimo;
    private int id_usuario;
    private int id_epi;
    private String data_retirada;
    private String data_prevista_devolucao;
    private String confirmacao_retirada;

    public Emprestimo(int id_emprestimo, int id_usuario, int id_epi, String data_retirada, String data_prevista_devolucao, String confirmacao_retirada) {
        this.id_emprestimo = id_emprestimo;
        this.id_usuario = id_usuario;
        this.id_epi = id_epi;
        this.data_retirada = data_retirada;
        this.data_prevista_devolucao = data_prevista_devolucao;
        this.confirmacao_retirada = confirmacao_retirada;
    }

    public int getId_emprestimo() {
        return id_emprestimo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_epi() {
        return id_epi;
    }

    public void setId_epi(int id_epi) {
        this.id_epi = id_epi;
    }

    public String getData_retirada() {
        return data_retirada;
    }

    public void setData_retirada(String data_retirada) {
        this.data_retirada = data_retirada;
    }

    public String getData_prevista_devolucao() {
        return data_prevista_devolucao;
    }

    public void setData_prevista_devolucao(String data_prevista_devolucao) {
        this.data_prevista_devolucao = data_prevista_devolucao;
    }

    public String getConfirmacao_retirada() {
        return confirmacao_retirada;
    }

    public void setConfirmacao_retirada(String confirmacao_retirada) {
        this.confirmacao_retirada = confirmacao_retirada;
    }
}

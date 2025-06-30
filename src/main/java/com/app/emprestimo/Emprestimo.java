package com.app.emprestimo;

import java.time.LocalDateTime;

/**
 * Classe que representa um empréstimo de EPI para um usuário.
 */
public class Emprestimo {
    private int id_emprestimo;
    private int id_usuario;
    private int id_epi;
    private LocalDateTime data_retirada;
    private LocalDateTime data_prevista_devolucao;
    private boolean confirmacao_retirada;

    /**
     * Construtor padrão (usado por frameworks).
     */
    public Emprestimo() {
    }

    /**
     * Construtor para novo empréstimo (sem ID).
     */
    public Emprestimo(int id_usuario, int id_epi, LocalDateTime data_retirada, LocalDateTime data_prevista_devolucao, boolean confirmacao_retirada) {
        this.id_usuario = id_usuario;
        this.id_epi = id_epi;
        this.data_retirada = data_retirada;
        this.data_prevista_devolucao = data_prevista_devolucao;
        this.confirmacao_retirada = confirmacao_retirada;
    }

    /**
     * Construtor completo.
     */
    public Emprestimo(int id_emprestimo, int id_usuario, int id_epi, LocalDateTime data_retirada, LocalDateTime data_prevista_devolucao, boolean confirmacao_retirada) {
        this.id_emprestimo = id_emprestimo;
        this.id_usuario = id_usuario;
        this.id_epi = id_epi;
        this.data_retirada = data_retirada;
        this.data_prevista_devolucao = data_prevista_devolucao;
        this.confirmacao_retirada = confirmacao_retirada;
    }

    // Getters e setters

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

    public LocalDateTime getData_retirada() {
        return data_retirada;
    }

    public void setData_retirada(LocalDateTime data_retirada) {
        this.data_retirada = data_retirada;
    }

    public LocalDateTime getData_prevista_devolucao() {
        return data_prevista_devolucao;
    }

    public void setData_prevista_devolucao(LocalDateTime data_prevista_devolucao) {
        this.data_prevista_devolucao = data_prevista_devolucao;
    }

    public boolean isConfirmacao_retirada() {
        return confirmacao_retirada;
    }

    public void setConfirmacao_retirada(boolean confirmacao_retirada) {
        this.confirmacao_retirada = confirmacao_retirada;
    }
}

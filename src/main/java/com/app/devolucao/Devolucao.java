package com.app.devolucao;

import java.time.LocalDateTime;

/**
 * Representa a devolução de um EPI emprestado.
 */
public class Devolucao {
    private int id_devolucao;
    private int id_emprestimo;
    private LocalDateTime data_devolucao;

    /**
     * Construtor padrão.
     */
    public Devolucao() {
    }

    /**
     * Construtor para criação de nova devolução (sem ID).
     */
    public Devolucao(int id_emprestimo, LocalDateTime data_devolucao) {
        this.id_emprestimo = id_emprestimo;
        this.data_devolucao = data_devolucao;
    }

    /**
     * Construtor completo.
     */
    public Devolucao(int id_devolucao, int id_emprestimo, LocalDateTime data_devolucao) {
        this.id_devolucao = id_devolucao;
        this.id_emprestimo = id_emprestimo;
        this.data_devolucao = data_devolucao;
    }

    public int getId_devolucao() {
        return id_devolucao;
    }

    public int getId_emprestimo() {
        return id_emprestimo;
    }

    public void setId_emprestimo(int id_emprestimo) {
        this.id_emprestimo = id_emprestimo;
    }

    public LocalDateTime getData_devolucao() {
        return data_devolucao;
    }

    public void setData_devolucao(LocalDateTime data_devolucao) {
        this.data_devolucao = data_devolucao;
    }
}

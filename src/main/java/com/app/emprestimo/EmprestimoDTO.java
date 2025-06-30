package com.app.emprestimo;

import java.time.LocalDateTime;

public class EmprestimoDTO {
    private String nomeEpi;
    private LocalDateTime dataRetirada;
    private LocalDateTime dataPrevistaDevolucao;
    private boolean confirmacaoRetirada;

    public EmprestimoDTO(String nomeEpi, LocalDateTime dataRetirada, LocalDateTime dataPrevistaDevolucao, boolean confirmacaoRetirada) {
        this.nomeEpi = nomeEpi;
        this.dataRetirada = dataRetirada;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.confirmacaoRetirada = confirmacaoRetirada;
    }

    public String getNomeEpi() {
        return nomeEpi;
    }

    public LocalDateTime getDataRetirada() {
        return dataRetirada;
    }

    public LocalDateTime getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public boolean isConfirmacaoRetirada() {
        return confirmacaoRetirada;
    }
}

package com.app.devolucao;

import java.time.LocalDateTime;

/**
 * DTO para exibir dados resumidos de devolução ao colaborador.
 */
public class DevolucaoDTO {
    private String nomeEpi;
    private LocalDateTime dataRetirada;
    private LocalDateTime dataDevolucao;

    public DevolucaoDTO(String nomeEpi, LocalDateTime dataRetirada, LocalDateTime dataDevolucao) {
        this.nomeEpi = nomeEpi;
        this.dataRetirada = dataRetirada;
        this.dataDevolucao = dataDevolucao;
    }

    public String getNomeEpi() {
        return nomeEpi;
    }

    public LocalDateTime getDataRetirada() {
        return dataRetirada;
    }

    public LocalDateTime getDataDevolucao() {
        return dataDevolucao;
    }
}

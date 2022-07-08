package com.soulcode.servicos.Models;

import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;

@Entity
public class Pagamento {

    @Id
    private Integer idPagamento;

    @NumberFormat(pattern = "#.##0,00")
    @Column(nullable = false)
    private double valor;

    @Column(nullable = false)
    private String formaPagamento;

    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagamento;

    public Integer getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(Integer idPagamento) {
        this.idPagamento = idPagamento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(StatusPagamento statusPagamento) {
        this.statusPagamento = statusPagamento;
    }
}

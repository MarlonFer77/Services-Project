package com.soulcode.servicos.Models;

public enum StatusPagamento {

    LANCADO("Lançado"),
    QUITADO("Quitado");

    private String status;

    StatusPagamento(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

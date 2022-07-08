package com.soulcode.servicos.Repositories;

import com.soulcode.servicos.Models.Chamados;
import com.soulcode.servicos.Models.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    @Query(value = "SELECT * FROM pagamento WHERE status_pagamento =:statusPagamento",nativeQuery = true)
    List<Pagamento> findByStatusPagamento(String statusPagamento);

    @Query(value = "SELECT pagamento.*, chamados.id_chamado, chamados.titulo, cliente.id_cliente, cliente.nome_cliente " +
            "FROM chamados RIGHT JOIN pagamento ON chamados.id_chamado = pagamento.id_pagamento " +
            "LEFT JOIN cliente ON cliente.id_cliente = chamados.id_cliente", nativeQuery = true)
    List<List> orcamentoComServicoCliente();
}

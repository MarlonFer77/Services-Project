package com.soulcode.servicos.Repositories;

import com.soulcode.servicos.Models.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
}

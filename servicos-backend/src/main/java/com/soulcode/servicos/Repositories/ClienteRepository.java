package com.soulcode.servicos.Repositories;


import com.soulcode.servicos.Models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByEmailCliente(String emailCliente);
    Optional<Cliente> deleteByNomeCliente(String nomeCliente);
}

package com.soulcode.servicos.Repositories;

import com.soulcode.servicos.Models.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {

    Optional<Funcionario> findByEmail(String email);

    // Optional<Funcionario> findByNomeAndEmail(String nome, String email);

}

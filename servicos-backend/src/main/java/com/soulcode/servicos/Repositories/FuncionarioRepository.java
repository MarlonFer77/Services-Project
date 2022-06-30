package com.soulcode.servicos.Repositories;

import com.soulcode.servicos.Models.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {

    Optional<Funcionario> findByEmail(String email);

    @Query(value = "SELECT * FROM funcionario WHERE id_cargo =:idCargo", nativeQuery = true)
    List<Funcionario> findByAllFuncionariosIdCargo(Integer idCargo);

    // Optional<Funcionario> findByNomeAndEmail(String nome, String email);

}

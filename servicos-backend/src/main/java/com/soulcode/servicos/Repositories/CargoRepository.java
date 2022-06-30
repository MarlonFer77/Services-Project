package com.soulcode.servicos.Repositories;

import com.soulcode.servicos.Models.Cargo;
import com.soulcode.servicos.Models.Chamados;
import com.soulcode.servicos.Models.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CargoRepository extends JpaRepository<Cargo, Integer> {

    @Query(value = "SELECT * FROM chamados WHERE status =:status", nativeQuery = true)
    List<Chamados> findByStatus(String status);

}

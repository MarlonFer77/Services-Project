package com.soulcode.servicos.Repositories;

import com.soulcode.servicos.Models.Chamados;
import com.soulcode.servicos.Models.Cliente;
import com.soulcode.servicos.Models.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ChamadosRepository extends JpaRepository<Chamados, Integer> {

    List<Chamados> findByCliente(Optional<Cliente> cliente);
    List<Chamados> findByFuncionario(Optional<Funcionario> funcionario);

    @Query(value = "SELECT * FROM chamados WHERE status =:status", nativeQuery = true)
    List<Chamados> findByStatus(String status);

    @Query(value = "SELECT * FROM chamados WHERE data_entrada BETWEEN :data1 AND :data2", nativeQuery = true)
    List<Chamados> findByIntervaloData(Date data1, Date data2);
}

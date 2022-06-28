package com.soulcode.servicos.Services;


import com.soulcode.servicos.Models.Chamados;
import com.soulcode.servicos.Models.Cliente;
import com.soulcode.servicos.Models.Funcionario;
import com.soulcode.servicos.Repositories.ChamadosRepository;
import com.soulcode.servicos.Repositories.ClienteRepository;
import com.soulcode.servicos.Repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChamadosServices {

    @Autowired
    ChamadosRepository chamadosRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    FuncionarioRepository funcionarioRepository;

    public List<Chamados> exibirChamado() {
        return chamadosRepository.findAll();
    }

    public  Chamados chamadoId(Integer idChamado){
        Optional<Chamados> chamados = chamadosRepository.findById(idChamado);
        return chamados.orElseThrow();
    }

    public  List<Chamados> buscarChamadosPeloCliente(Integer idCliente){
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        return chamadosRepository.findByCliente(cliente);
    }

    public  List<Chamados> buscarChamadosPeloFuncionario(Integer idFuncionario){
        Optional<Funcionario> funcionario = funcionarioRepository.findById(idFuncionario);
        return chamadosRepository.findByFuncionario(funcionario);
    }

    public List<Chamados> buscarChamadosPeloStatus(String status){
        return chamadosRepository.findByStatus(status);
    }

    public List<Chamados> buscarPorIntervaloData(Date data1, Date data2) {
        return chamadosRepository.findByIntervaloData(data1, data2);
    }
}

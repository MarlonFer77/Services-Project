package com.soulcode.servicos.Services;


import com.soulcode.servicos.Models.Funcionario;
import com.soulcode.servicos.Repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Quando se fala em serviços, estamos falando dos métodos do CRUD da tabela

@Service
public class FuncionarioServices {
    // aqui se faz a injeção de dependência
    @Autowired
    FuncionarioRepository funcionarioRepository;

    //primeiro serviço na tabela de funcionário, vai ser a leitura de todos os funcionários cadastrados
    // findAll -> método do spring Data JPA -> busca todos os registros de uma tabela

    public List<Funcionario> mostrarTodosFuncionarios() {
        return funcionarioRepository.findAll();
    }

    // vamos criar mais um serviço relacionado ao funcionário
    // criar um serviço de buscar apenas um funcionário pelo seu id(chave primária)

    public Funcionario mostrarUmFuncionarioPeloId(Integer idFuncionario) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(idFuncionario);
        return funcionario.orElseThrow();
    }

    // vamos criar mais um serviço para buscar um funcionário pelo seu email

    public Funcionario mostrarUmFuncionarioPeloEmail(String emailFuncionario) {
        Optional<Funcionario> funcionario = funcionarioRepository.findByEmail(emailFuncionario);
        return funcionario.orElseThrow();
    }

    // vamos criar um servioço para cadastrar um novo funcionário

    public Funcionario cadastrarFuncionario(Funcionario funcionario) {
        // só por precaução, nós vamos colocar o id do funcionario como nulo
        funcionario.setIdFuncionario(null);
        return funcionarioRepository.save(funcionario);
    }

    public void excluirFuncionario(Integer idFuncionario){
        funcionarioRepository.deleteById(idFuncionario);
    }

    public Funcionario editarFuncionario(Funcionario funcionario){
        return funcionarioRepository.save(funcionario);
    }

    public  Funcionario salvarFoto(Integer idFuncionario, String caminhoFoto){
        Funcionario funcionario = mostrarUmFuncionarioPeloId(idFuncionario);
        funcionario.setFoto(caminhoFoto);

        return funcionarioRepository.save(funcionario);
    }
}
package com.soulcode.servicos.Services;


import com.soulcode.servicos.Models.Cargo;
import com.soulcode.servicos.Models.Funcionario;
import com.soulcode.servicos.Repositories.CargoRepository;
import com.soulcode.servicos.Repositories.FuncionarioRepository;
import com.soulcode.servicos.Services.Exceptions.DataIntegrityViolationException;
import com.soulcode.servicos.Services.Exceptions.EntityNoteFoundException;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Quando se fala em serviços, estamos falando dos métodos do CRUD da tabela

@Service
public class FuncionarioServices {
    // aqui se faz a injeção de dependência
    @Autowired
    FuncionarioRepository funcionarioRepository;
    @Autowired
    CargoRepository cargoRepository;


    //primeiro serviço na tabela de funcionário, vai ser a leitura de todos os funcionários cadastrados
    // findAll -> método do spring Data JPA -> busca todos os registros de uma tabela

    @Cacheable("funcCache")
    public List<Funcionario> mostrarTodosFuncionarios() {
        return funcionarioRepository.findAll();
    }

    // vamos criar mais um serviço relacionado ao funcionário
    // criar um serviço de buscar apenas um funcionário pelo seu id(chave primária)

    @Cacheable(value = "funcCache", key = "#idFuncionario")
    public Funcionario mostrarUmFuncionarioPeloId(Integer idFuncionario) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(idFuncionario);
        return funcionario.orElseThrow(
                () -> new EntityNoteFoundException("Funcionário não encontrado: " + idFuncionario)
        );
    }

    // vamos criar mais um serviço para buscar um funcionário pelo seu email


    public Funcionario mostrarUmFuncionarioPeloEmail(String emailFuncionario) {
        Optional<Funcionario> funcionario = funcionarioRepository.findByEmail(emailFuncionario);
        return funcionario.orElseThrow();
    }

    // vamos criar um servioço para cadastrar um novo funcionário

    @CachePut(value = "funcCache", key = "#funcionario.idFuncionario")
    public Funcionario cadastrarFuncionario(Funcionario funcionario, Integer idCargo) {
        // só por precaução, nós vamos colocar o id do funcionario como nulo
        try {
            Cargo cargo = cargoRepository.findById(idCargo).get();
            funcionario.setCargo(cargo);
            return funcionarioRepository.save(funcionario);
        }catch (Exception e){
            throw new DataIntegrityViolationException("Erro ao cadastrar funcionário");
        }
    }

    @CacheEvict(value = "funcCache", key = "#idFuncionario")
    public void excluirFuncionario(Integer idFuncionario){
        funcionarioRepository.deleteById(idFuncionario);
    }

    @CachePut(value = "funcCache", key = "#funcionario.idFuncionario")
    public Funcionario editarFuncionario(Funcionario funcionario){
        return funcionarioRepository.save(funcionario);
    }

    @CachePut(value = "funcCache", key = "#idFuncionario")
    public  Funcionario salvarFoto(Integer idFuncionario, String caminhoFoto){
        Funcionario funcionario = mostrarUmFuncionarioPeloId(idFuncionario);
        funcionario.setFoto(caminhoFoto);

        return funcionarioRepository.save(funcionario);
    }

    @CachePut(value = "funcCache", key = "#funcionario.idFuncionario")
    public Funcionario atribuirCargo(Integer idCargo, Funcionario funcionario){

        // Buscar o cargo
        // setar o cargo ao funcionário
        // salvar

        Optional<Cargo> cargo = cargoRepository.findById(idCargo);
        funcionario.setCargo(cargo.get());

        return funcionarioRepository.save(funcionario);
    }

    @Cacheable(value = "funcCache", key = "#idCargo")
    public List<Funcionario> mostrarFuncionarioPeloCargo(Integer idCargo){
        return funcionarioRepository.findByAllFuncionariosIdCargo(idCargo);
    }


}
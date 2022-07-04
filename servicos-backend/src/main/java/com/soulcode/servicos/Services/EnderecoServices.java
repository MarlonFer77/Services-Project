package com.soulcode.servicos.Services;

import com.soulcode.servicos.Models.Endereco;
import com.soulcode.servicos.Repositories.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoServices {

    @Autowired
    EnderecoRepository enderecoRepository;

    public List<Endereco> buscarTodosEnderecos(){
        return enderecoRepository.findAll();
    }

    public Endereco cadastrarEndereco(Endereco endereco){
        return enderecoRepository.save(endereco);
    }

    public void deletarEndereco(Integer idEndereco){
    enderecoRepository.deleteById(idEndereco);
    }

    public Endereco editarEndereco(Endereco endereco){
    return enderecoRepository.save(endereco);
    }
}

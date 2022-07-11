package com.soulcode.servicos.Services;

import com.soulcode.servicos.Models.Cliente;
import com.soulcode.servicos.Models.Endereco;
import com.soulcode.servicos.Repositories.ClienteRepository;
import com.soulcode.servicos.Repositories.EnderecoRepository;
import com.soulcode.servicos.Services.Exceptions.EntityNoteFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServices {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    EnderecoRepository enderecoRepository;

    public List<Cliente> exibirCliente(){
        return clienteRepository.findAll();
    }

    public Cliente exibirIdCliente(Integer idCliente){
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        return cliente.orElseThrow(
                () -> new EntityNoteFoundException("Cliente não encontrado: " + idCliente)
        );
    }

    public Cliente exibirEmailCliente(String emailCliente){
        Optional<Cliente> cliente = clienteRepository.findByEmailCliente(emailCliente);

        return cliente.orElseThrow();
    }

    public Cliente cadastrarCliente(Cliente cliente){
        cliente.setIdCliente(null);
        return clienteRepository.save(cliente);
    }

    public void deletarClientePeloId(Integer idCliente){
        clienteRepository.deleteById(idCliente);
    }

//    public void deletarClientePeloNome(String nomeCliente){
//        clienteRepository.deleteByNomeCliente(nomeCliente);
//    }

    public void editarCliente(Cliente cliente){
        clienteRepository.save(cliente);
    }


}

package com.soulcode.servicos.Services;

import ch.qos.logback.core.net.server.Client;
import com.soulcode.servicos.Models.Cliente;
import com.soulcode.servicos.Models.Endereco;
import com.soulcode.servicos.Models.Funcionario;
import com.soulcode.servicos.Repositories.ClienteRepository;
import com.soulcode.servicos.Repositories.EnderecoRepository;
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
        return cliente.orElseThrow();
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

    public Cliente atribuirEndereco(Integer idEndereco, Cliente cliente){

        Optional<Endereco> endereco = enderecoRepository.findById(idEndereco);
        cliente.setEndereco(endereco.get());

        return clienteRepository.save(cliente);
    }
}

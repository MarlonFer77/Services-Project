package com.soulcode.servicos.Services;

import com.soulcode.servicos.Models.Cliente;
import com.soulcode.servicos.Models.Endereco;
import com.soulcode.servicos.Repositories.ClienteRepository;
import com.soulcode.servicos.Repositories.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoServices {

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Cacheable("enderecoCache")
    public List<Endereco> buscarTodosEnderecos(){
        return enderecoRepository.findAll();
    }

    @Cacheable(value = "enderecoCache", key = "#idEndereco")
    public Endereco buscarEnderecosPeloId(Integer idEndereco){
        Optional<Endereco> endereco = enderecoRepository.findById(idEndereco);
        return endereco.orElseThrow();
    }

    // regras 1 -> para cadastrar um endereço, o cliente ja deve estar cadastrado no database
    //        2 -> no momento do cadastro do endereço, precisamos passar o id do cliente que é o dono desse endereço
    //        3 -> o id do endereço vai ser o mesmo id do cliente
    //        4 -> não permitir que um endereço seja salvo sem a existência do respectivo cliente
    @CachePut(value = "enderecoCache", key = "#idCliente")
    public Endereco cadastrarEndereco(Endereco endereco, Integer idCliente) throws Exception {
        // estamos declarando um optional de cliente e atribuindo para este os dados do cliente que receberá o
        // novo endereço
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        if (cliente.isPresent()) {   // isPresent -> vai verificar se existe dados dentro de algo, nesse cado, se existe um cliente com o id passado.
            endereco.setIdEndereco(idCliente); // busco o endereço e seto o id do cliente pra ele
            enderecoRepository.save(endereco); // salvo o endereço com o id do cliente

            cliente.get().setEndereco(endereco); // busco o cliente e seto o endereço nele
            clienteRepository.save(cliente.get()); // salvo o cliente com o endereço setado
            return endereco;
        } else {
            throw new Exception();
        }
    }

    @CachePut(value = "enderecoCache", key = "#endereco.idEndereco")
    public Endereco editarEndereco(Endereco endereco){
    return enderecoRepository.save(endereco);
    }
}

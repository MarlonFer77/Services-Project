package com.soulcode.servicos.Controllers;

import com.soulcode.servicos.Models.Endereco;
import com.soulcode.servicos.Services.EnderecoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("servicos")
public class EnderecoController {

    @Autowired
    EnderecoServices enderecoServices;

    @GetMapping("/enderecos")
    public List<Endereco> buscarTodosEnderecos(){
        List<Endereco> enderecos = enderecoServices.buscarTodosEnderecos();
        return enderecos;
    }

    @PostMapping("/enderecos")
    public ResponseEntity<Endereco> cadastrarEndereco(@RequestBody Endereco endereco){

        endereco = enderecoServices.cadastrarEndereco(endereco);
        URI novaUri = ServletUriComponentsBuilder.fromCurrentRequest().path("id")
                .buildAndExpand(endereco.getIdEndereco()).toUri();
        return ResponseEntity.created(novaUri).body(endereco);
    }

    @DeleteMapping("/enderecos/{idEndereco}")
    public ResponseEntity<Void> deletarEndereco(@PathVariable Integer idEndereco){
        enderecoServices.deletarEndereco(idEndereco);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/enderecos/{idEndereco}")
    public ResponseEntity<Endereco> editarEndereco (@PathVariable Integer idEndereco, @RequestBody Endereco endereco){
        endereco.setIdEndereco(idEndereco);
        enderecoServices.editarEndereco(endereco);
        return ResponseEntity.ok().build();
    }
}

package com.soulcode.servicos.Controllers;


import ch.qos.logback.core.net.server.Client;
import com.soulcode.servicos.Models.Cliente;
import com.soulcode.servicos.Services.ClienteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("servicos")
public class ClienteController {

    @Autowired
    ClienteServices clienteServices;

    @GetMapping("/cliente")
    public List<Cliente> exibirCliente() {
        List<Cliente> clientes = clienteServices.exibirCliente();
        return clientes;
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<Cliente> exibirIdCliente(@PathVariable Integer idCliente) {
        Cliente cliente = clienteServices.exibirIdCliente(idCliente);
        return ResponseEntity.ok()
                .body(cliente);
    }

    @GetMapping("/clienteEmail/{emailCliente}")
    public ResponseEntity<Cliente> exibirEmailCliente(@PathVariable String emailCliente) {
        Cliente cliente = clienteServices.exibirEmailCliente(emailCliente);
        return ResponseEntity.ok()
                .body(cliente);
    }

    @PostMapping("/cliente")
    public ResponseEntity<Cliente> cadastrarCliente(@RequestBody Cliente cliente) {
        cliente = clienteServices.cadastrarCliente(cliente);
        URI newUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("id")
                .buildAndExpand(cliente.getIdCliente())
                .toUri();

        return ResponseEntity.created(newUri)
                .body(cliente);
    }

    @DeleteMapping("/cliente/{idCliente}")
    public ResponseEntity<Void> deletarClientePeloId(@PathVariable Integer idCliente) {
        clienteServices.deletarClientePeloId(idCliente);
        return ResponseEntity.noContent()
                .build();
    }

//    @DeleteMapping("/clienteNome/{emailCliente}")
//    public ResponseEntity<Void> deletarClientePeloEmail(@PathVariable String emailCliente) {
//        clienteServices.deletarClientePeloEmail(emailCliente);
//        return ResponseEntity.noContent().build();
//    }

    @PutMapping("/cliente/{idCliente}")
    public ResponseEntity<Cliente> editarCliente(@PathVariable Integer idCliente, @RequestBody Cliente cliente) {
        cliente.setIdCliente(idCliente);
        clienteServices.editarCliente(cliente);
        return ResponseEntity.ok()
                .build();
    }
}
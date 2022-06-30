package com.soulcode.servicos.Controllers;


import com.soulcode.servicos.Models.Chamados;
import com.soulcode.servicos.Models.Funcionario;
import com.soulcode.servicos.Models.StatusChamado;
import com.soulcode.servicos.Services.ChamadosServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("servicos")
public class ChamadosController {

    @Autowired
    ChamadosServices chamadosServices;

    @GetMapping("/chamados")
    public List<Chamados> exibirChamado() {
        List<Chamados> chamados = chamadosServices.exibirChamado();
        return chamados;
    }

    @GetMapping("/chamados/{idChamado}")
    public ResponseEntity<Chamados> chamadoId(@PathVariable Integer idChamado) {
        Chamados chamado = chamadosServices.chamadoId(idChamado);
        return ResponseEntity.ok().body(chamado);
    }

    @GetMapping("/chamadosPeloCliente/{idCliente}")
    public List<Chamados> buscarChamadosPeloCliente(@PathVariable Integer idCliente) {
        List<Chamados> chamados = chamadosServices.buscarChamadosPeloCliente(idCliente);
        return chamados;
    }

    @GetMapping("/chamadosPeloFuncionario/{idFuncionario}")
    public List<Chamados> buscarChamadosPeloFuncionario(@PathVariable Integer idFuncionario) {
        List<Chamados> chamados = chamadosServices.buscarChamadosPeloFuncionario(idFuncionario);
        return chamados;
    }

    @GetMapping("/chamadosPeloStatus")
    public List<Chamados> buscarChamadosPeloStatus(@RequestParam("status") String status ) {
        List<Chamados> chamados = chamadosServices.buscarChamadosPeloStatus(status);
        return chamados;
    }

    @GetMapping("/chamadosPorIntervaloData")
    public List<Chamados> buscarPorIntervaloData(
            @RequestParam("data1")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date data1,
            @RequestParam("data2") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date data2
    ){
        List<Chamados> chamados = chamadosServices.buscarPorIntervaloData(data1, data2);
        return chamados;
    }

    // aqui vamos de definir o endpoint para o serviço de cadastro de um novo chamado
    // para cadastro precisamos anotar como método http - post
    @PostMapping("/chamados/{idCliente}")
    public ResponseEntity<Chamados> cadastrarChamado(@PathVariable Integer idCliente, @RequestBody Chamados chamados){
        chamados = chamadosServices.cadastrarChamados(chamados, idCliente);
        // nesse momento o chamado já foi cadastrado no database
        // precisamos agora criar o caminho (uri) para que esse novo chamado possa ser acessado
        URI novaUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(chamados.getIdChamado()).toUri();
        return ResponseEntity.created(novaUri).body(chamados);
    }

    // Vamos mapear o serviço de excluir um chamado
    @DeleteMapping("/chamados/{idChamado}")
    public ResponseEntity<Void> excluirChamados(@PathVariable Integer idChamado){
        chamadosServices.excluirChamado(idChamado);
        return ResponseEntity.noContent().build();
    }

    // Vamos mapear o serviço de editar um chamado
    // para ediçao precisamos do método http do tipo PUT
    @PutMapping("/chamados/{idChamado}")
    public ResponseEntity<Chamados> editarChamados(@PathVariable Integer idChamado, @RequestBody Chamados chamados){
        chamados.setIdChamado(idChamado);
        chamadosServices.editarChamados(chamados, idChamado);
        return ResponseEntity.ok().build();
    }

    // Vamos fazer o mapeamento do método de atribuir um funcionário a um determinado chamado
    @PutMapping("/chamadosAtribuirFuncionario/{idChamado}/{idFuncionario}")
    public ResponseEntity<Chamados> atribuirFuncionario(@PathVariable Integer idChamado, @PathVariable Integer idFuncionario){
        chamadosServices.atribuirFuncionario(idChamado, idFuncionario);
        return ResponseEntity.noContent().build();
    }

    // Vamos construir o mapeamento do método para modificar o status de um chamado
    @PutMapping("/chamadosModificarStatus/{idChamado}")
    public ResponseEntity<Chamados> modificarStatus(@PathVariable Integer idChamado, @RequestParam("status") String status){
        chamadosServices.modificarStatus(idChamado, status);
        return ResponseEntity.ok().build();
    }
}

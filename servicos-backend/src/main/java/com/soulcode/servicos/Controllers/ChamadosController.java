package com.soulcode.servicos.Controllers;


import com.soulcode.servicos.Models.Chamados;
import com.soulcode.servicos.Services.ChamadosServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}

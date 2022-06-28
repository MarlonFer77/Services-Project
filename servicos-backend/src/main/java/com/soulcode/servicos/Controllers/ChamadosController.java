package com.soulcode.servicos.Controllers;


import com.soulcode.servicos.Models.Chamados;
import com.soulcode.servicos.Services.ChamadosServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Chamados> chamadoId(@PathVariable Integer idCliente) {
        Chamados chamado = chamadosServices.chamadoId(idCliente);
        return ResponseEntity.ok().body(chamado);
    }
}

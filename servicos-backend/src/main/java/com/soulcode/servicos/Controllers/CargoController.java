package com.soulcode.servicos.Controllers;


import com.soulcode.servicos.Models.Cargo;
import com.soulcode.servicos.Services.CargoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.Servlet;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("servicos")
public class CargoController {

    @Autowired
    CargoServices cargoServices;

    @GetMapping("/cargos")
    public List<Cargo> exibirTodosOsCargosService(){
        List<Cargo> cargo = cargoServices.exibirTodosOsCargos();
        return cargo;
    }

    @GetMapping("/cargos/{idCargo}")
    public ResponseEntity<Cargo> exibirCargoPeloIdService(@PathVariable Integer idCargo){
        Cargo cargo = cargoServices.exibirCargoPeloId(idCargo);
        return ResponseEntity.ok().body(cargo);
    }

    @DeleteMapping("/cargos/{idCargo}")
    public ResponseEntity<Void> deletarCargoService(@PathVariable Integer idCargo){
        cargoServices.deletarCargo(idCargo);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cargos")
    public ResponseEntity<Cargo> cadastrarCargoService(@RequestBody Cargo cargo){
        cargo = cargoServices.cadastrarCargo(cargo);
        URI novaUri = ServletUriComponentsBuilder.fromCurrentRequest().path("id")
                .buildAndExpand(cargo.getIdCargo()).toUri();
        return ResponseEntity.created(novaUri).body(cargo);
    }

    @PutMapping("/cargos/{idCargo}")
    public ResponseEntity<Cargo> editarCargo(@PathVariable Integer idCargo, @RequestBody Cargo cargo){
        cargo.setIdCargo(idCargo);
        cargoServices.editarCargo(cargo);

        return ResponseEntity.ok().body(cargo);
    }



}

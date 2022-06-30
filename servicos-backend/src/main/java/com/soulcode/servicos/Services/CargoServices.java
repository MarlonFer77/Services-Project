package com.soulcode.servicos.Services;

import com.soulcode.servicos.Models.Cargo;
import com.soulcode.servicos.Repositories.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CargoServices {

    @Autowired
    CargoRepository cargoRepository;

    public List<Cargo> exibirTodosOsCargos(){
        return cargoRepository.findAll();
    }

    public Cargo exibirCargoPeloId(Integer idCargo){
        Optional<Cargo> cargo = cargoRepository.findById(idCargo);
        return cargo.orElseThrow();
    }

    public void deletarCargo(Integer idCargo){
        cargoRepository.deleteById(idCargo);
    }

    public Cargo cadastrarCargo(Cargo cargo){
        // cargo.setIdCargo(null);
        return cargoRepository.save(cargo);
    }

    public Cargo editarCargo(Cargo cargo){
        return cargoRepository.save(cargo);
    }
}

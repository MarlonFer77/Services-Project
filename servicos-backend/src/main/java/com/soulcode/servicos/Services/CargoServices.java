package com.soulcode.servicos.Services;

import com.soulcode.servicos.Models.Cargo;
import com.soulcode.servicos.Repositories.CargoRepository;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CargoServices {

    @Autowired
    CargoRepository cargoRepository;

    @Cacheable("cargoCache")
    public List<Cargo> exibirTodosOsCargos(){
        return cargoRepository.findAll();
    }

    @Cacheable(value = "cargoCache", key = "#idCargo")
    public Cargo exibirCargoPeloId(Integer idCargo){
        Optional<Cargo> cargo = cargoRepository.findById(idCargo);
        return cargo.orElseThrow();
    }

    @CacheEvict(value = "cargoCache", key = "#idCargo", allEntries = true)
    public void deletarCargo(Integer idCargo){
        cargoRepository.deleteById(idCargo);
    }

    @CachePut(value = "cargoCache", key = "#cargo.idCargo")
    public Cargo cadastrarCargo(Cargo cargo){
        // cargo.setIdCargo(null);
        return cargoRepository.save(cargo);
    }

    @CachePut(value = "cargoCache", key = "#cargo.idCargo")
    public Cargo editarCargo(Cargo cargo){
        return cargoRepository.save(cargo);
    }
}

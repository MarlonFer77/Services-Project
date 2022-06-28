package com.soulcode.servicos.Services;


import com.soulcode.servicos.Models.Chamados;
import com.soulcode.servicos.Repositories.ChamadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChamadosServices {

    @Autowired
    ChamadosRepository chamadosRepository;

    public List<Chamados> exibirChamado() {
        return chamadosRepository.findAll();
    }

    public  Chamados chamadoId(Integer idChamado){
        Optional<Chamados> chamados = chamadosRepository.findById(idChamado);
        return chamados.orElseThrow();
    }
}

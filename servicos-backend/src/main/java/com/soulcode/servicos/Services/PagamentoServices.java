package com.soulcode.servicos.Services;

import com.soulcode.servicos.Models.Chamados;
import com.soulcode.servicos.Models.Pagamento;
import com.soulcode.servicos.Models.StatusPagamento;
import com.soulcode.servicos.Repositories.ChamadosRepository;
import com.soulcode.servicos.Repositories.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagamentoServices {

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    ChamadosRepository chamadosRepository;

    @Cacheable("pagamentoCache")
    public List<Pagamento> buscarTodosOsPagamentos(){
        return pagamentoRepository.findAll();
    }

    @Cacheable(value = "pagamentoCache", key = "#idPagamento")
    public Pagamento buscarPagamentoPeloId(Integer idPagamento){
        Optional<Pagamento> pagamento = pagamentoRepository.findById(idPagamento);
        return pagamento.orElseThrow();
    }

    @CachePut(value = "pagamentoCache", key = "#idChamado")
    public Pagamento cadastrarPagamento(Pagamento pagamento, Integer idChamado){
        Optional<Chamados> chamados = chamadosRepository.findById(idChamado);

        if (chamados.isPresent()){
            pagamento.setIdPagamento(idChamado);
            pagamento.setStatusPagamento(StatusPagamento.LANCADO);
            pagamentoRepository.save(pagamento);

            chamados.get().setPagamento(pagamento); // busca o chamado e seta o pagamento nele
            chamadosRepository.save(chamados.get()); // salvando o chamado com o pagamento já setado
            return pagamento;
        }else{
            throw new RuntimeException();
        }

    }

    @CachePut(value = "pagamentoCache", key = "#idPagamento")
    public Pagamento modificarStatus(Integer idPagamento, String statusPagamento) {
        Pagamento pagamento = buscarPagamentoPeloId(idPagamento);

        switch (statusPagamento) {

            case "QUITADO": {
                pagamento.setStatusPagamento(StatusPagamento.QUITADO);
                break;
            }
            case "LANCADO": {
                pagamento.setStatusPagamento(StatusPagamento.LANCADO);
                break;

            }
        }
        return pagamentoRepository.save(pagamento);
    }

    @CachePut(value = "pagamentoCache", key = "#pagamento.idPagamento")
    public Pagamento editarPagamento(Pagamento pagamento){
        return pagamentoRepository.save(pagamento);
    }

    @Cacheable(value = "pagamentoCache", key = "#statusPagamento")
    public List<Pagamento> buscarPagamentosPeloStatus(String statusPagamento){
        return pagamentoRepository.findByStatusPagamento(statusPagamento);
    }

    @Cacheable(value = "pagamentoCache")
    public List<List> orcamentoComServicoCliente() {
        return pagamentoRepository.orcamentoComServicoCliente();
    }

}

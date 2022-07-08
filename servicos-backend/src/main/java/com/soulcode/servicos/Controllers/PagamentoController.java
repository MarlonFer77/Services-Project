package com.soulcode.servicos.Controllers;

import com.soulcode.servicos.Models.Pagamento;
import com.soulcode.servicos.Services.PagamentoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("servicos")
public class PagamentoController {

    @Autowired
    PagamentoServices pagamentoServices;

    @GetMapping("/pagamentos")
    public List<Pagamento> buscarTodosOsPagamento(){
        return pagamentoServices.buscarTodosOsPagamentos();
    }

    @GetMapping("/pagamentos/{idPagamento}")
    public ResponseEntity<Pagamento> buscarPagamentoPeloId(@PathVariable Integer idPagamento){
        Pagamento pagamento = pagamentoServices.buscarPagamentoPeloId(idPagamento);
        return ResponseEntity.ok().body(pagamento);
    }

    @PostMapping("/pagamentos/{idChamado}")
    public ResponseEntity<Pagamento> cadastrarPagamento(
            @RequestBody Pagamento pagamento,
            @PathVariable Integer idChamado){

        pagamento = pagamentoServices.cadastrarPagamento(pagamento, idChamado);

        URI novaUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(pagamento.getIdPagamento()).toUri(); // ACERTEI

        return ResponseEntity.created(novaUri).body(pagamento);
    }

    @PutMapping("/pagamentosModificarStatus/{idPagamento}")
    public ResponseEntity<Pagamento> modificarStatus(@PathVariable Integer idPagamento,
                                                     @RequestParam("statusPagamento") String status){
        pagamentoServices.modificarStatus(idPagamento, status);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/pagamentos/{idPagamento}")
    public ResponseEntity<Pagamento> editarPagamento(@PathVariable Integer idPagamento, @RequestBody Pagamento pagamento){
        pagamento.setIdPagamento(idPagamento);
        pagamentoServices.editarPagamento(pagamento);
        return ResponseEntity.ok().body(pagamento);
    }
    @GetMapping("/pagamentosPeloStatus")
    public List<Pagamento> buscarPagamentosPeloStatus(@RequestParam("statusPagamento") String statusPagamento){
        List<Pagamento> pagamentos = pagamentoServices.buscarPagamentosPeloStatus(statusPagamento);
        return pagamentos;
    }

    @GetMapping("/pagamentosChamadosComCliente")
    public List<List> orcamentoComServicoCliente(){
        return pagamentoServices.orcamentoComServicoCliente();
    }
}

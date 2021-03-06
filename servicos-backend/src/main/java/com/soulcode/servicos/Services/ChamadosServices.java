package com.soulcode.servicos.Services;


import ch.qos.logback.core.net.server.Client;
import com.soulcode.servicos.Models.Chamados;
import com.soulcode.servicos.Models.Cliente;
import com.soulcode.servicos.Models.Funcionario;
import com.soulcode.servicos.Models.StatusChamado;
import com.soulcode.servicos.Repositories.ChamadosRepository;
import com.soulcode.servicos.Repositories.ClienteRepository;
import com.soulcode.servicos.Repositories.FuncionarioRepository;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChamadosServices {

    @Autowired
    ChamadosRepository chamadosRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Cacheable("chamadosCache")
    public List<Chamados> exibirChamado() {
        return chamadosRepository.findAll();
    }

    @Cacheable(value = "chamadosCache", key = "#idChamado")
    public  Chamados chamadoId(Integer idChamado){
        Optional<Chamados> chamados = chamadosRepository.findById(idChamado);
        return chamados.orElseThrow();
    }

    @Cacheable(value = "chamadosCache", key = "idCliente")
    public  List<Chamados> buscarChamadosPeloCliente(Integer idCliente){
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        return chamadosRepository.findByCliente(cliente);
    }

    @Cacheable(value = "chamadosCache", key = "#idFuncionario")
    public  List<Chamados> buscarChamadosPeloFuncionario(Integer idFuncionario){
        Optional<Funcionario> funcionario = funcionarioRepository.findById(idFuncionario);
        return chamadosRepository.findByFuncionario(funcionario);
    }

    @Cacheable(value = "chamadosCache", key = "#status")
    public List<Chamados> buscarChamadosPeloStatus(String status){
        return chamadosRepository.findByStatus(status);
    }

    @Cacheable(value = "chamadosCache")
    public List<Chamados> buscarPorIntervaloData(Date data1, Date data2) {
        return chamadosRepository.findByIntervaloData(data1, data2);
    }

    // cadaastrar um novo chamado
    // temos 2 regras:
    // 1) no momento do cadastro do chamado j?? devemos informar de qual cliente ??
    // 2) no momento do cadastro do chamado, a princ??pio vamos fazer esse cadastro sem estar atribu??do  a um funcion??rio
    // 3) no momento do cadastro do chamado, o status desse chamado deve ser  RECEBIDO

    // Servi??o para cadastro de novo chamado
    @CachePut(value = "chamadosCache", key = "#chamados.idChamado")
    public Chamados cadastrarChamados(Chamados chamados, Integer idCliente) {
        chamados.setStatus(StatusChamado.RECEBIDO); // regra 3 - atribui????o do statsu recebido para o chamado que est?? sendo cadastrado
        chamados.setFuncionario(null); // regra 2 - dizer que ainda n??o atribu??mos esse chamado para nenhum funcion??rio
        Optional<Cliente> cliente = clienteRepository.findById(idCliente); // regra 1 - buscando os dados do cliente dono do chamado
        chamados.setCliente(cliente.get());
        return chamadosRepository.save(chamados);
    }

    // M??todo para exclus??o de um chamado
    @CacheEvict(value = "chamadosCache", key = "#idChamado", allEntries = true)
    public void excluirChamado(Integer idChamado) {
        chamadosRepository.deleteById(idChamado);
    }

    // M??todo para editar um chamado
    // no momento da edi????o de um chamado, devemos preservar o cliente e o funcion??rio desse chamado
    // vamos editar os dados do chamado, mas continuamos com os dados do cliente e os dados do funcion??rio
    @CachePut(value = "chamadosCache", key = "#idChamado")
    public Chamados editarChamados(Chamados chamado, Integer idChamado){
        // instaciamos aqui um objeto do tipo Chamado para guardar os dados dos chamados
        // sem as novas altera????es
        Chamados chamadoSemAsNovasAlteracoes = chamadoId(idChamado);
        Funcionario funcionario = chamadoSemAsNovasAlteracoes.getFuncionario();
        Cliente cliente = chamadoSemAsNovasAlteracoes.getCliente();

        chamado.setCliente(cliente);
        chamado.setFuncionario(funcionario);
        return chamadosRepository.save(chamado);
    }

    // M??todo para atribuir um funcion??rio para um determinado chamado
    // ou trocar o funcion??rio de determinado chamado
    // => regra -> no momento em que um determinado chamado ?? atribu??do a um funcion??rio
    //             o status do chamado precisa ser alterado para ATRIBUIDO
    @CachePut(value = "chamadosCache", key = "#idChamado")
    public Chamados atribuirFuncionario(Integer idChamado, Integer idFuncionario) {
        // Buscar os dados do funcion??rio que vai ser atribu??do a esse chamado
        Optional<Funcionario> funcionario = funcionarioRepository.findById(idFuncionario);
        // Buscar o chamado para o qual vai ser especificado o funcion??rio escolhido
        Chamados chamados = chamadoId(idChamado);
        chamados.setFuncionario(funcionario.get());
        chamados.setStatus(StatusChamado.ATRIBUIDO);

        return chamadosRepository.save(chamados);
    }

    // M??todo para modificar o status de um chamado

    @CachePut(value = "chamadosCache", key = "#idChamado")
    public Chamados modificarStatus(Integer idChamado, String status) {
        Chamados chamados = chamadoId(idChamado);

        if (chamados.getFuncionario() != null){
            switch (status){
                case "ATRIBUIDO": {
                    chamados.setStatus(StatusChamado.ATRIBUIDO);
                    break;
                }
                case "CONCLUIDO": {
                    chamados.setStatus(StatusChamado.CONCLUIDO);
                    break;
                }
                case "ARQUIVADO": {
                    chamados.setStatus(StatusChamado.ARQUIVADO);
                    break;
                }
            }
        }
        switch (status) {
            case "RECEBIDO": {
                chamados.setStatus(StatusChamado.RECEBIDO);
                break;
            }
        }
        return chamadosRepository.save(chamados);
    }
}

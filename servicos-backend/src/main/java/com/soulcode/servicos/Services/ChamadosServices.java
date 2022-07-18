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

    @Cacheable(value = "chamadosCache", key = "#data1")
    public List<Chamados> buscarPorIntervaloData(Date data1, Date data2) {
        return chamadosRepository.findByIntervaloData(data1, data2);
    }

    // cadaastrar um novo chamado
    // temos 2 regras:
    // 1) no momento do cadastro do chamado já devemos informar de qual cliente é
    // 2) no momento do cadastro do chamado, a princípio vamos fazer esse cadastro sem estar atribuído  a um funcionário
    // 3) no momento do cadastro do chamado, o status desse chamado deve ser  RECEBIDO

    // Serviço para cadastro de novo chamado
    @CachePut(value = "chamadosCache", key = "#chamados.idChamado")
    public Chamados cadastrarChamados(Chamados chamados, Integer idCliente) {
        chamados.setStatus(StatusChamado.RECEBIDO); // regra 3 - atribuição do statsu recebido para o chamado que está sendo cadastrado
        chamados.setFuncionario(null); // regra 2 - dizer que ainda não atribuímos esse chamado para nenhum funcionário
        Optional<Cliente> cliente = clienteRepository.findById(idCliente); // regra 1 - buscando os dados do cliente dono do chamado
        chamados.setCliente(cliente.get());
        return chamadosRepository.save(chamados);
    }

    // Método para exclusão de um chamado
    @CacheEvict(value = "chamadosCache", key = "#idChamado")
    public void excluirChamado(Integer idChamado) {
        chamadosRepository.deleteById(idChamado);
    }

    // Método para editar um chamado
    // no momento da edição de um chamado, devemos preservar o cliente e o funcionário desse chamado
    // vamos editar os dados do chamado, mas continuamos com os dados do cliente e os dados do funcionário
    @CachePut(value = "chamadosCache", key = "#idChamado")
    public Chamados editarChamados(Chamados chamado, Integer idChamado){
        // instaciamos aqui um objeto do tipo Chamado para guardar os dados dos chamados
        // sem as novas alterações
        Chamados chamadoSemAsNovasAlteracoes = chamadoId(idChamado);
        Funcionario funcionario = chamadoSemAsNovasAlteracoes.getFuncionario();
        Cliente cliente = chamadoSemAsNovasAlteracoes.getCliente();

        chamado.setCliente(cliente);
        chamado.setFuncionario(funcionario);
        return chamadosRepository.save(chamado);
    }

    // Método para atribuir um funcionário para um determinado chamado
    // ou trocar o funcionário de determinado chamado
    // => regra -> no momento em que um determinado chamado é atribuído a um funcionário
    //             o status do chamado precisa ser alterado para ATRIBUIDO
    @CachePut(value = "chamadosCache", key = "#idChamado")
    public Chamados atribuirFuncionario(Integer idChamado, Integer idFuncionario) {
        // Buscar os dados do funcionário que vai ser atribuído a esse chamado
        Optional<Funcionario> funcionario = funcionarioRepository.findById(idFuncionario);
        // Buscar o chamado para o qual vai ser especificado o funcionário escolhido
        Chamados chamados = chamadoId(idChamado);
        chamados.setFuncionario(funcionario.get());
        chamados.setStatus(StatusChamado.ATRIBUIDO);

        return chamadosRepository.save(chamados);
    }

    // Método para modificar o status de um chamado

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

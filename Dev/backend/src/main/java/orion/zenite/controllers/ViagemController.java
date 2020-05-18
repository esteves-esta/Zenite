package orion.zenite.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import orion.zenite.dto.ViagemDto;
import orion.zenite.entidades.*;
import orion.zenite.repositorios.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.*;

@Api(description = "Operações relacionadas a viagem", tags = "viagem")
@RestController
@RequestMapping("/api/viagem")
public class ViagemController {

    @Autowired
    private ViagemRepository repository;

    @Autowired
    private FiscalRepository fiscalRepository;

    @Autowired
    private MotoristaRepository motoristaRepository;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private LinhaRepository linhaRepository;


    @ApiOperation("Lista todos as viagens")
    @GetMapping
    public ResponseEntity consulta() {

        if (this.repository.count() > 0) {
            return ok(this.repository.findAll());
        } else {
            return noContent().build();
        }

    }

    @ApiOperation("Exibe ônibus por id")
    @GetMapping("{id}")
    public ResponseEntity consultar(@PathVariable("id") Integer id) {
        Optional<Viagem> consultaViagem = this.repository.findById(id);

        if (consultaViagem.isPresent()) {
            return ok(consultaViagem);
        } else {
            return notFound().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluirViagem(@PathVariable("id") Integer id) {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
            return ok().build();
        } else {
            return notFound().build();
        }
    }

    /*Gráfico com início e fim de viagem
    Gráfico mostrando tempo percorrido na viagem
    Gráfico com média de tempo de viagem por linha
    Gráfico com horários dos dias em que as linhas mais demoram em suas viagens
    Gráfico mostrando quantidade de ônibus circulando por linha em um determinado período

     */

    @ApiOperation("Pesquisar viagem pelo id da linha")
    @GetMapping("/linha/{id}")
    public ResponseEntity consultarPorLinha(@PathVariable("id") Integer id) {
        Optional<Linha> linha = linhaRepository.findById(id);
        if (linha.isPresent()) {
            List<Viagem> consultaViagem = this.repository.findByLinha(linha.get());
            if (!consultaViagem.isEmpty()) {
                return ok(consultaViagem);
            }
        }

        return notFound().build();
    }

    @ApiOperation("Pesquisar viagem pelo id do ônibus")
    @GetMapping("/onibus/{id}")
    public ResponseEntity consultarPorOnibus(@PathVariable("id") Integer id) {
        Optional<Carro> carro = carroRepository.findById(id);
        if (carro.isPresent()) {
            List<Viagem> consultaViagem = this.repository.findByCarro(carro.get());
            if (!consultaViagem.isEmpty()) {
                return ok(consultaViagem);
            }
        }

        return notFound().build();
    }

    @ApiOperation("Pesquisar viagem pelo id do motorista")
    @GetMapping("/motorista/{id}")
    public ResponseEntity consultarPorMotorista(@PathVariable("id") Integer id) {
        Optional<Motorista> motorista = motoristaRepository.findById(id);
        if (motorista.isPresent()) {
            List<Viagem> consultaViagem = this.repository.findByMotorista(motorista.get());
            if (!consultaViagem.isEmpty()) {
                return ok(consultaViagem);
            }
        }

        return notFound().build();
    }

    @ApiOperation("Altera uma viagem")
    @PutMapping("{id}")
    public ResponseEntity alterar(@RequestBody ViagemDto viagem,
                                  @PathVariable Integer id) {
        viagem.setViagemId(id);
        Viagem novaViagem = montaViagem(viagem);
        novaViagem.setId(viagem.getViagemId());

        Optional<Viagem> v = repository.findById(viagem.getViagemId());
        if (novaViagem == null || !v.isPresent()) {
            return badRequest().build();
        } else {
            this.repository.save(novaViagem);
            return ok().build();
        }
    }

    @ApiOperation("Cadastra uma viagem")
    @PostMapping
    @Transactional // se acontece algum error desfaz os outros dados salvos, faz um rollback
    public ResponseEntity cadastrar(@RequestBody ViagemDto viagem) {
        Viagem novaViagem = montaViagem(viagem);

        if (novaViagem == null) {
            return badRequest().build();
        } else {
            this.repository.save(novaViagem);
            return created(null).build();
        }
    }

    public Viagem montaViagem(ViagemDto viagem) {
        Viagem novaViagem = new Viagem();

        Optional<Fiscal> f = fiscalRepository.findById(viagem.getFiscalId());
        Fiscal fiscal = f.orElse(null);
        novaViagem.setFiscal(fiscal);

        Optional<Linha> l = linhaRepository.findById(viagem.getLinhaId());
        Linha linha = l.orElse(null);
        novaViagem.setLinha(linha);

        Optional<Motorista> m = motoristaRepository.findById(viagem.getMotoristaId());
        Motorista motorista = m.orElse(null);
        novaViagem.setMotorista(motorista);

        Optional<Carro> c = carroRepository.findById(viagem.getCarroId());
        Carro carro = c.orElse(null);
        novaViagem.setCarro(carro);

        novaViagem.setHoraChegada(viagem.getHoraChegada());
        novaViagem.setHoraSaida(viagem.getHoraSaida());
        novaViagem.setQtdPassageiros(viagem.getQtdPassageiros());

        if (fiscal == null || linha == null || carro == null || motorista == null) {
            return null;
        } else {
            return novaViagem;
        }
    }

}

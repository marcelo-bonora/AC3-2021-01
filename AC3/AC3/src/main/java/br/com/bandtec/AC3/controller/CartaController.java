package br.com.bandtec.AC3.controller;

import br.com.bandtec.AC3.adapter.CartaAdapter;
import br.com.bandtec.AC3.interator.FilaObj;
import br.com.bandtec.AC3.interator.ListaObj;
import br.com.bandtec.AC3.model.Carta;
import br.com.bandtec.AC3.interator.PilhaObj;
import br.com.bandtec.AC3.repository.CartaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/cartas")
public class CartaController {

    @Autowired
    private CartaRepository repository;

    PilhaObj<Carta> pilhaCartaDeletada = new PilhaObj(5);

    FilaObj<CartaAdapter> filaCartaRequisicao = new FilaObj(5);

    ListaObj<CartaAdapter> listaCartaTratada = new ListaObj(5);

    @GetMapping
    public ResponseEntity getCartas(){
        List<Carta> cartas = repository.findAll();

        if(cartas.isEmpty()){
            return ResponseEntity.status(204).build();

        } else {
            return ResponseEntity.status(200).body(repository.findAll());

        }
    }

    @PostMapping
    public ResponseEntity postCarta(@RequestBody @Valid Carta novoCarta) {
            repository.save(novoCarta);
            return ResponseEntity.status(201).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCarta(@PathVariable int id){
        if (repository.existsById(id)){
            pilhaCartaDeletada.push(repository.getById(id));
            repository.deleteById(id);
            return ResponseEntity.status(200).build();

        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity putCarta(@PathVariable int id, @RequestBody Carta carta){
        if(repository.existsById(id)){
            carta.setIdCarta(id);
            repository.save(carta);
            return ResponseEntity.status(201).build();

        } else {
            return ResponseEntity.status(400).build();
        }
    }

//    ------------- Métodos Desfazer --------------------

    @PostMapping("/desfazerDelete")
    public ResponseEntity desfazerDelete(){
        if(!pilhaCartaDeletada.isEmpty()){
            repository.save(pilhaCartaDeletada.pop());
            return ResponseEntity.status(201).build();

        } else {
            return ResponseEntity.status(204).body("Não há Delete para desfazer");
        }
    }

//    ----------- Métodos de Requisição Assíncrona ----------

    @GetMapping("/requisicao")
    public ResponseEntity getRequisicao(@RequestBody @Valid Carta carta){
        Integer cod = ThreadLocalRandom.current().nextInt(1, 100);
        CartaAdapter cartaAdapter = new CartaAdapter(cod, carta);

        filaCartaRequisicao.insert(cartaAdapter);
        return ResponseEntity.status(200).body(cod);
    }

    @PostMapping("/tratamento")
    public ResponseEntity tratamento(){

        if(!filaCartaRequisicao.isEmpty()){
            listaCartaTratada.adiciona(filaCartaRequisicao.peek());
            postCarta(filaCartaRequisicao.poll().getCarta());
            return ResponseEntity.status(201).build();

        } else {
            return ResponseEntity.status(204).body("Sem tratamentos pendentes");
        }

    }

    @GetMapping("/tratamento/{cod}")
    public ResponseEntity consultarTratamento(@PathVariable int cod){

        for(int i = 0; i < listaCartaTratada.getTamanho(); i++){
            if(listaCartaTratada.getElemento(i).getId().equals(cod)){
                listaCartaTratada.removePeloIndice(i);
                return ResponseEntity.status(200).build();
            }
        }
        return ResponseEntity.status(204).build();
    }

}

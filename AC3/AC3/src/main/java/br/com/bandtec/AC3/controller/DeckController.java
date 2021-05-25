package br.com.bandtec.AC3.controller;

import br.com.bandtec.AC3.adapter.CartaAdapter;
import br.com.bandtec.AC3.adapter.DeckAdapter;
import br.com.bandtec.AC3.interator.FilaObj;
import br.com.bandtec.AC3.interator.ListaObj;
import br.com.bandtec.AC3.model.Carta;
import br.com.bandtec.AC3.model.Deck;
import br.com.bandtec.AC3.interator.PilhaObj;
import br.com.bandtec.AC3.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/decks")
public class DeckController {

    @Autowired
    private DeckRepository repository;

    PilhaObj<Deck> pilhaDeckDeletada = new PilhaObj(5);

    FilaObj<DeckAdapter> filaDeckRequisicao = new FilaObj(5);

    ListaObj<DeckAdapter> listaDeckTratada = new ListaObj(5);

    @GetMapping
    public ResponseEntity getDecks(){
        List<Deck> decks = repository.findAll();

        if (decks.isEmpty()){
            return ResponseEntity.status(204).build();

        } else {
            return ResponseEntity.status(200).body(repository.findAll());

        }
    }

    @PostMapping
    public ResponseEntity postCarta(@RequestBody @Valid Deck novoDeck) {
            repository.save(novoDeck);
            return ResponseEntity.status(201).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCarta(@PathVariable int id){
        if (repository.existsById(id)){
            pilhaDeckDeletada.push(repository.getById(id));
            repository.deleteById(id);
            return ResponseEntity.status(200).build();

        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity putCarta(@PathVariable int id, @RequestBody Deck deck){
        if(repository.existsById(id)){
            deck.setIdDeck(id);
            repository.save(deck);
            return ResponseEntity.status(201).build();

        } else {
            return ResponseEntity.status(400).build();
        }
    }

//    ------------- Métodos Desfazer --------------------

    @PostMapping("/desfazerDelete")
    public ResponseEntity desfazerDelete(){
        if(!pilhaDeckDeletada.isEmpty()){
            repository.save(pilhaDeckDeletada.pop());
            return ResponseEntity.status(201).build();

        } else {
            return ResponseEntity.status(204).body("Não há Delete para desfazer");
        }
    }

    //    ----------- Métodos de Requisição Assíncrona ----------

    @GetMapping("/requisicao")
    public ResponseEntity getRequisicao(@RequestBody @Valid Deck deck){
        Integer cod = ThreadLocalRandom.current().nextInt(1, 100);
        DeckAdapter deckAdapter = new DeckAdapter(cod, deck);

        filaDeckRequisicao.insert(deckAdapter);
        return ResponseEntity.status(200).body(cod);
    }

    @PostMapping("/tratamento")
    public ResponseEntity tratamento(){

        if(!filaDeckRequisicao.isEmpty()){
            listaDeckTratada.adiciona(filaDeckRequisicao.peek());
            postCarta(filaDeckRequisicao.poll().getDeck());
            return ResponseEntity.status(201).build();

        } else {
            return ResponseEntity.status(204).body("Sem tratamentos pendentes");
        }

    }

    @GetMapping("/tratamento/{cod}")
    public ResponseEntity consultarTratamento(@PathVariable int cod){

        for(int i = 0; i < listaDeckTratada.getTamanho(); i++){
            if(listaDeckTratada.getElemento(i).getId().equals(cod)){
                listaDeckTratada.removePeloIndice(i);
                return ResponseEntity.status(200).build();
            }
        }
        return ResponseEntity.status(204).build();
    }

}

package br.com.bandtec.AC3.controller;

import br.com.bandtec.AC3.adapter.DeckAdapter;
import br.com.bandtec.AC3.iterator.FilaObj;
import br.com.bandtec.AC3.iterator.ListaObj;
import br.com.bandtec.AC3.model.Deck;
import br.com.bandtec.AC3.iterator.PilhaObj;
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

    private static PilhaObj<Deck> pilhaDeckDeletada = new PilhaObj(5);

    private static FilaObj<DeckAdapter> filaDeckRequisicao = new FilaObj(5);

    private static ListaObj<DeckAdapter> listaDeckTratada = new ListaObj(5);

    public static PilhaObj<Deck> getPilhaDeckDeletada() {
        return pilhaDeckDeletada;
    }

    public static FilaObj<DeckAdapter> getFilaDeckRequisicao() {
        return filaDeckRequisicao;
    }

    public static ListaObj<DeckAdapter> getListaDeckTratada() {
        return listaDeckTratada;
    }

    @GetMapping
    public ResponseEntity<List<Deck>> getDecks(){
        List<Deck> decks = repository.findAll();
        return decks.isEmpty()
            ? ResponseEntity.status(204).build()
            : ResponseEntity.status(200).body(decks);
    }

    @PostMapping
    public ResponseEntity postDeck(@RequestBody @Valid Deck novoDeck) {
            repository.save(novoDeck);
            return ResponseEntity.status(201).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteDeck(@PathVariable int id){
        if (repository.existsById(id)){
            pilhaDeckDeletada.push(repository.getById(id));
            repository.deleteById(id);
            return ResponseEntity.status(200).build();

        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity putDeck(@PathVariable int id, @RequestBody Deck deck){
        if(repository.existsById(id)){
            deck.setIdDeck(id);
            repository.save(deck);
            return ResponseEntity.status(200).build();

        } else {
            return ResponseEntity.status(404).build();
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

    @PostMapping("/requisicao")
    public ResponseEntity postRequisicao(@RequestBody @Valid Deck deck){
        Integer cod = ThreadLocalRandom.current().nextInt(1, 1000);
        DeckAdapter deckAdapter = new DeckAdapter(cod, deck);

        filaDeckRequisicao.insert(deckAdapter);
        return ResponseEntity.status(200).body(cod);
    }

    @GetMapping("/tratamento/{cod}")
    public ResponseEntity consultarTratamento(@PathVariable int cod){

        for(int i = 0; i < listaDeckTratada.getTamanho(); i++){
            if(listaDeckTratada.getElemento(i).getId().equals(cod)){
                DeckAdapter deckTratado = new DeckAdapter(
                        listaDeckTratada.getElemento(i).getId(),
                        listaDeckTratada.getElemento(i).getDeck()
                );
                listaDeckTratada.removePeloIndice(i);
                return ResponseEntity.status(200).body(deckTratado);
            }
        }
        return ResponseEntity.status(404).build();
    }

}

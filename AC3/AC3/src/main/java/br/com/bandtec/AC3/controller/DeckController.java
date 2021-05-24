package br.com.bandtec.AC3.controller;

import br.com.bandtec.AC3.model.Deck;
import br.com.bandtec.AC3.interator.PilhaObj;
import br.com.bandtec.AC3.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/decks")
public class DeckController {

    @Autowired
    private DeckRepository repository;

//    PilhaObj<Deck> pilhaDeckInserida = new PilhaObj(100);
    PilhaObj<Deck> pilhaDeckDeletada = new PilhaObj(100);

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
//            pilhaDeckInserida.push(novoDeck);
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
//            pilhaDeckInserida.push(pilhaDeckDeletada.pop());
            repository.save(pilhaDeckDeletada.pop());
            return ResponseEntity.status(201).build();

        } else {
            return ResponseEntity.status(204).body("Não há Delete para desfazer");
        }
    }

//    @DeleteMapping("/desfazerPost")
//    public ResponseEntity desfazerPost(){
//        if(!pilhaDeckInserida.isEmpty()){
//            pilhaDeckDeletada.push(pilhaDeckInserida.pop());
//            repository.delete(pilhaDeckDeletada.peek());
//            return ResponseEntity.status(200).build();
//
//        } else {
//            return ResponseEntity.status(204).body("Não há Post para desfazer");
//        }
//    }

}

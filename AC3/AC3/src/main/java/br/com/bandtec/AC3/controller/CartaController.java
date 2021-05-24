package br.com.bandtec.AC3.controller;

import br.com.bandtec.AC3.model.Carta;
import br.com.bandtec.AC3.interator.PilhaObj;
import br.com.bandtec.AC3.repository.CartaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cartas")
public class CartaController {

    @Autowired
    private CartaRepository repository;

//    PilhaObj<Carta> pilhaCartaInserida = new PilhaObj(100);
    PilhaObj<Carta> pilhaCartaDeletada = new PilhaObj(100);


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
//            pilhaCartaInserida.push(novoCarta);
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
            carta.setId(id);
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
//            pilhaCartaInserida.push(pilhaCartaDeletada.pop());
            repository.save(pilhaCartaDeletada.pop());
            return ResponseEntity.status(201).build();

        } else {
            return ResponseEntity.status(204).body("Não há Delete para desfazer");
        }
    }

//    @DeleteMapping("/desfazerPost")
//    public ResponseEntity desfazerPost(){
//        if(!pilhaCartaInserida.isEmpty()){
//            pilhaCartaDeletada.push(pilhaCartaInserida.pop());
//            repository.delete(pilhaCartaDeletada.peek());
//            return ResponseEntity.status(200).build();
//
//        } else {
//            return ResponseEntity.status(204).body("Não há Post para desfazer");
//        }
//    }

}

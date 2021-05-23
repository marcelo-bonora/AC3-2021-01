package br.com.bandtec.AC3.Controller;

import br.com.bandtec.AC3.Model.Carta;
import br.com.bandtec.AC3.PilhaObj;
import br.com.bandtec.AC3.Repository.CartaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/cartas")
public class CartaController {

    PilhaObj<Carta> pilhaCarta = new PilhaObj<>(100);

    @Autowired
    private CartaRepository repository;

    @GetMapping
    public ResponseEntity getCatas(){
        return ResponseEntity.status(200).body(repository.findAll());
    }

    @PostMapping
    public ResponseEntity postCarta(@RequestBody Carta novoCarta) {
        pilhaCarta.push(novoCarta);
        repository.save(novoCarta);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping()
    public ResponseEntity desfazer(){
        if(!pilhaCarta.isEmpty()){
            repository.deleteById(pilhaCarta.pop().getCod());
            return ResponseEntity.status(200).build();

        } else {
            return ResponseEntity.status(404).build();
        }
    }

}

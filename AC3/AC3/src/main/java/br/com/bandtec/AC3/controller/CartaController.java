package br.com.bandtec.AC3.controller;

import br.com.bandtec.AC3.adapter.CartaAdapter;
import br.com.bandtec.AC3.iterator.FilaObj;
import br.com.bandtec.AC3.iterator.ListaObj;
import br.com.bandtec.AC3.model.Carta;
import br.com.bandtec.AC3.iterator.PilhaObj;
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

    private static PilhaObj<Carta> pilhaCartaDeletada = new PilhaObj(5);

    private static FilaObj<CartaAdapter> filaCartaRequisicao = new FilaObj(5);

    private static ListaObj<CartaAdapter> listaCartaTratada = new ListaObj(5);

    public static PilhaObj<Carta> getPilhaCartaDeletada() {
        return pilhaCartaDeletada;
    }

    public static FilaObj<CartaAdapter> getFilaCartaRequisicao() {
        return filaCartaRequisicao;
    }

    public static ListaObj<CartaAdapter> getListaCartaTratada() {
        return listaCartaTratada;
    }

    @GetMapping
    public ResponseEntity<List<Carta>> getCartas(){
        List<Carta> cartas = repository.findAll();
        return cartas.isEmpty()
            ? ResponseEntity.status(204).build()
            : ResponseEntity.status(200).body(cartas);
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
            return ResponseEntity.status(200).build();

        } else {
            return ResponseEntity.status(404).build();
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

    @PostMapping("/requisicao")
    public ResponseEntity postRequisicao(@RequestBody @Valid Carta carta){
        Integer cod = ThreadLocalRandom.current().nextInt(1, 1000);
        CartaAdapter cartaAdapter = new CartaAdapter(cod, carta);

        filaCartaRequisicao.insert(cartaAdapter);
        return ResponseEntity.status(200).body(cod);
    }

    @GetMapping("/tratamento/{cod}")
    public ResponseEntity consultarTratamento(@PathVariable int cod){

        for(int i = 0; i < listaCartaTratada.getTamanho(); i++){
            if(listaCartaTratada.getElemento(i).getId().equals(cod)){
                CartaAdapter cartaTratada = new CartaAdapter(
                        listaCartaTratada.getElemento(i).getId(),
                        listaCartaTratada.getElemento(i).getCarta()
                );
                listaCartaTratada.removePeloIndice(i);
                return ResponseEntity.status(200).body(cartaTratada);
            }
        }
        return ResponseEntity.status(404).build();
    }

}

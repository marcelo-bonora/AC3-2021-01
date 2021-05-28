package br.com.bandtec.AC3.controller;

import br.com.bandtec.AC3.document.Arquivo;
import br.com.bandtec.AC3.interator.ListaObj;
import br.com.bandtec.AC3.model.Deck;
import br.com.bandtec.AC3.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    @Autowired
    private DeckRepository deckRepository;

    Arquivo arquivo = new Arquivo();

    @GetMapping
    public ResponseEntity getDocumento(){
        ListaObj<Deck> listaDeck = new ListaObj(100);
        for (int i = 0; i < deckRepository.count(); i++){
            listaDeck.adiciona(deckRepository.getById(i+1));
        }
        arquivo.gravaLista(listaDeck, "documentLayout");
        return ResponseEntity.status(200).build();
    }


}

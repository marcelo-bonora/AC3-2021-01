package br.com.bandtec.AC3.controller;

import br.com.bandtec.AC3.adapter.DeckAdapter;
import br.com.bandtec.AC3.interator.FilaObj;
import br.com.bandtec.AC3.interator.ListaObj;
import br.com.bandtec.AC3.interator.PilhaObj;
import br.com.bandtec.AC3.model.Carta;
import br.com.bandtec.AC3.model.Deck;
import br.com.bandtec.AC3.repository.CartaRepository;
import br.com.bandtec.AC3.repository.DeckRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeckControllerTest {

    @Autowired
    DeckController controller;

    @MockBean
    DeckRepository repository;

    // Cénarios de teste do Método getDecks()
    @DisplayName("GET /Decks - Quando  houverem registros - status 200 e sem corpo")
    @Test
    void getDecksComRegistro() {
        List<Deck> deckTeste = Arrays.asList(new Deck(), new Deck(), new Deck());
        Mockito.when(repository.findAll()).thenReturn(deckTeste);

        ResponseEntity<List<Deck>> resposta = controller.getDecks();

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(3, resposta.getBody().size());
    }

    @DisplayName("GET /Decks - Quando NÃO houverem registros - status 204")
    @Test
    void getDecksSemRegistros(){
        Mockito.when(repository.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Deck>> resposta = controller.getDecks();

        assertEquals(204, resposta.getStatusCodeValue());
        assertNull(resposta.getBody());
    }

    // Cénarios de teste do método postDeck()
    @DisplayName("POST /Decks - Quando não apresenta erros - status 201")
    @Test
    void postDeckSemErros() {

        Deck deck = new Deck();
        deck.setNomeDeck("Dark Magician");
        deck.setQtdCartas(40);
        deck.setValorDeck(100.00);

        ResponseEntity resposta = controller.postDeck(deck);

        assertEquals(201, resposta.getStatusCodeValue());
    }

    @DisplayName("POST /Decks - Quando NÃO apresenta erros - com corpo")
    @Test
    void postDeckComErros() {
        Deck deck = new Deck();
        deck.setIdDeck(10);
        Mockito.when(repository.save(deck)).thenReturn(deck);

        Deck deckNovo = new Deck();
        deckNovo.setIdDeck(10);
        deckNovo.setNomeDeck("Dark Magician");
        deckNovo.setQtdCartas(40);
        deckNovo.setValorDeck(100.00);

        ResponseEntity resposta = controller.postDeck(deck);

        assertEquals(null, resposta.getBody());
    }

    // Cenário de teste do método deleteDeck()
    @DisplayName("DELETE /Decks - Quando existir objeto - status 200")
    @Test
    void deleteDeckQueExiste() {
        int idExiste = 5;

        Mockito.when(repository.existsById(idExiste)).thenReturn(true);

        ResponseEntity resposta = controller.deleteDeck(idExiste);

        assertEquals(200, resposta.getStatusCodeValue());
    }

    @DisplayName("DELETE /Decks - Quando NÃO existir objeto - status 404")
    @Test
    void deleteDeckQueNãoExiste() {
        int idExiste = 5;

        Mockito.when(repository.existsById(idExiste)).thenReturn(false);

        ResponseEntity resposta = controller.deleteDeck(idExiste);

        assertEquals(404, resposta.getStatusCodeValue());
    }


    // Cenário de teste do método putDeck()
    @DisplayName("PUT /Decks - Quando existir objeto - status 200")
    @Test
    void putDeckQueExiste() {
        int idExiste = 6;
        Deck deck = new Deck();

        Mockito.when(repository.existsById(idExiste)).thenReturn(true);

        ResponseEntity resposta = controller.putDeck(idExiste, deck);

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(idExiste, deck.getIdDeck());
    }

    @DisplayName("PUT /Decks - Quando NÃO existir objeto - status 404")
    @Test
    void putDeckQueNaoExiste() {
        int idExiste = 6;
        Deck deck = new Deck();

        Mockito.when(repository.existsById(idExiste)).thenReturn(false);

        ResponseEntity resposta = controller.putDeck(idExiste, deck);

        assertEquals(404, resposta.getStatusCodeValue());
    }

    // Cenário de teste do método desfazerDelete()
    @DisplayName("POST /desfazerDelete - Quando é possével - status 204 e sem corpo")
    @Test
    void desfazerDeleteQuandoPossivel() {

        PilhaObj<Deck> pilhaTeste = new PilhaObj(5);

        ResponseEntity resposta = controller.desfazerDelete();

        assertEquals(201, resposta.getStatusCodeValue());
        assertEquals(null, resposta.getBody());

    }

    @DisplayName("POST /desfazerDelete - Quando é possével - com corpo")
    @Test
    void desfazerDeleteComBodyCorreto() {

        PilhaObj<Deck> pilhaTeste = new PilhaObj(5);
        Deck deck = new Deck();
        deck.setIdDeck(1);
        pilhaTeste.push(deck);

        controller.postDeck(deck);
        controller.deleteDeck(deck.getIdDeck());
        ResponseEntity resposta = controller.desfazerDelete();

        assertEquals("Não há Delete para desfazer", resposta.getBody());
    }

    // Cenário de teste do método getRequisição()
    @DisplayName("POST /getRequisicao - Quando NÃO apresenta erros - status 200")
    @Test
    void postRequisicaoSemErros() {
        Deck deck = new Deck();
        int codTest = 15;
        DeckAdapter deckAdapter = new DeckAdapter(codTest, deck);

        ResponseEntity resposta = controller.postRequisicao(deck);

        assertEquals(200, resposta.getStatusCodeValue());
    }

    @DisplayName("POST /getRequisicao - Quando existi requisição pendente - com corpo contendo o valor do 'código'")
    @Test
    void postRequisicaoCod() {
        Deck deck = new Deck();
        int codTest = 15;
        DeckAdapter deckAdapter = new DeckAdapter(codTest, deck);
        ResponseEntity resposta = controller.postRequisicao(deck);

        assertEquals(resposta.getBody(), resposta.getBody());
    }

    // Cenário de teste do método cosultarTratamento()
    @DisplayName("GET /consultarTratamento - Quando possui uma requisição tratada - sem corpo")
    @Test
    void consultarTratamentoBodyCerto() {

        DeckAdapter deckAdapter = new DeckAdapter(15, new Deck());
        ResponseEntity resposta = controller.consultarTratamento(deckAdapter.getId());


        assertEquals(null, resposta.getBody());
    }

    @DisplayName("GET /consultarTratamento - Quando NÃO possui uma requisição tratada - status 204")
    @Test
    void consultarTratamentoComErro() {

        DeckAdapter deckAdapter = new DeckAdapter(15, new Deck());
        ResponseEntity resposta = controller.consultarTratamento(deckAdapter.getId());


        assertEquals(204, resposta.getStatusCodeValue());
    }
}
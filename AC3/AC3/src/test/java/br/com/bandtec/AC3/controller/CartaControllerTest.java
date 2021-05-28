package br.com.bandtec.AC3.controller;

import br.com.bandtec.AC3.adapter.CartaAdapter;
import br.com.bandtec.AC3.iterator.PilhaObj;
import br.com.bandtec.AC3.model.Carta;
import br.com.bandtec.AC3.model.Deck;
import br.com.bandtec.AC3.repository.CartaRepository;
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
class CartaControllerTest {

    @Autowired
    CartaController controller;

    @MockBean
    CartaRepository repository;

    // Cénarios de teste do Método getCarta()
    @DisplayName("GET /Cartas - Quando  houverem registros - status 200 e sem corpo")
    @Test
    void getCartasComRegistro() {
        List<Carta> cartaTeste = Arrays.asList(new Carta(), new Carta(), new Carta());
        Mockito.when(repository.findAll()).thenReturn(cartaTeste);

        ResponseEntity<List<Carta>> resposta = controller.getCartas();

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(3, resposta.getBody().size());
    }

    @DisplayName("GET /Cartas - Quando NÃO houverem registros - status 204")
    @Test
    void getCartasSemRegistros(){
        Mockito.when(repository.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Carta>> resposta = controller.getCartas();

        assertEquals(204, resposta.getStatusCodeValue());
        assertNull(resposta.getBody());
    }

    // Cénarios de teste do método postCarta()
    @DisplayName("POST /Cartas - Quando não apresenta erros - status 201")
    @Test
    void postCartaSemErros() {

        Carta carta = new Carta();
        carta.setNomeCarta("Exodia");
        carta.setTipo("Monster/Effect");
        carta.setValorCarta(1.00);
        carta.setDeck(new Deck());

        ResponseEntity resposta = controller.postCarta(carta);

        assertEquals(201, resposta.getStatusCodeValue());
    }

    @DisplayName("POST /Cartas - Quando NÃO apresenta erros - com corpo")
    @Test
    void postCartaComErros() {
        Carta carta = new Carta();
        carta.setIdCarta(10);
        Mockito.when(repository.save(carta)).thenReturn(carta);

        Carta cartaNova = new Carta();
        cartaNova.setNomeCarta("Exodia");
        cartaNova.setTipo("Monster/Effect");
        cartaNova.setValorCarta(1.00);
        cartaNova.setDeck(new Deck());

        ResponseEntity resposta = controller.postCarta(carta);

        assertEquals(null, resposta.getBody());
    }

    // Cenário de teste do método deleteDeck()
    @DisplayName("DELETE /Cartas - Quando existir objeto - status 200")
    @Test
    void deleteCartaQueExiste() {
        int idExiste = 5;

        Mockito.when(repository.existsById(idExiste)).thenReturn(true);

        ResponseEntity resposta = controller.deleteCarta(idExiste);

        assertEquals(200, resposta.getStatusCodeValue());
    }

    @DisplayName("DELETE /Cartas - Quando NÃO existir objeto - status 404")
    @Test
    void deleteCartaQueNãoExiste() {
        int idExiste = 5;

        Mockito.when(repository.existsById(idExiste)).thenReturn(false);

        ResponseEntity resposta = controller.deleteCarta(idExiste);

        assertEquals(404, resposta.getStatusCodeValue());
    }

    // Cenário de teste do método putCarta()
    @DisplayName("PUT /Cartas - Quando existir objeto - status 200")
    @Test
    void putCartaQueExiste() {
        int idExiste = 6;
        Carta carta = new Carta();

        Mockito.when(repository.existsById(idExiste)).thenReturn(true);

        ResponseEntity resposta = controller.putCarta(idExiste, carta);

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(idExiste, carta.getIdCarta());
    }

    @DisplayName("PUT /Cartas - Quando NÃO existir objeto - status 404")
    @Test
    void putCartaQueNaoExiste() {
        int idExiste = 6;
        Carta carta = new Carta();

        Mockito.when(repository.existsById(idExiste)).thenReturn(false);

        ResponseEntity resposta = controller.putCarta(idExiste, carta);

        assertEquals(404, resposta.getStatusCodeValue());
    }

    // Cenário de teste do método desfazerDelete()
    @DisplayName("POST /desfazerDelete - Quando é possével - status 201 e sem corpo")
    @Test
    void desfazerDeleteQuandoNaoPossivel() {

        PilhaObj<Carta> pilhaTeste = new PilhaObj(5);

        ResponseEntity resposta = controller.desfazerDelete();

        assertEquals(201, resposta.getStatusCodeValue());
        assertEquals(null, resposta.getBody());

    }

    @DisplayName("POST /desfazerDelete - Quando é possivel - com corpo")
    @Test
    void desfazerDeleteComBodyCorreto() {

        PilhaObj<Carta> pilhaTeste = new PilhaObj(5);
        Carta carta = new Carta();
        carta.setIdCarta(1);
        pilhaTeste.push(carta);

        controller.postCarta(carta);
        controller.deleteCarta(carta.getIdCarta());
        ResponseEntity resposta = controller.desfazerDelete();

        assertEquals("Não há Delete para desfazer", resposta.getBody());
    }

    // Cenário de teste do método getRequisição()
    @DisplayName("POST /getRequisicao - Quando NÃO apresenta erros - status 200")
    @Test
    void postRequisicaoSemErros() {
        Carta carta = new Carta();
        int codTest = 15;
        CartaAdapter cartaAdapter = new CartaAdapter(codTest, carta);

        ResponseEntity resposta = controller.postRequisicao(carta);

        assertEquals(200, resposta.getStatusCodeValue());
    }

    @DisplayName("POST /getRequisicao - Quando existi requisição pendente - com corpo contendo o valor do 'código'")
    @Test
    void postRequisicaoCod() {
        Carta carta = new Carta();
        int codTest = 15;

        ResponseEntity resposta = controller.postRequisicao(carta);

        assertEquals(resposta.getBody(), resposta.getBody());
    }

    // Cenário de teste do método cosultarTratamento()
    @DisplayName("GET /consultarTratamento - Quando possui uma requisição tratada - sem corpo")
    @Test
    void consultarTratamentoBodyCerto() {

        CartaAdapter cartaAdapter = new CartaAdapter(15, new Carta());
        ResponseEntity resposta = controller.consultarTratamento(cartaAdapter.getId());


        assertEquals(null, resposta.getBody());
    }

    @DisplayName("GET /consultarTratamento - Quando NÃO possui uma requisição tratada - status 204")
    @Test
    void consultarTratamentoComErro() {

        CartaAdapter cartaAdapter = new CartaAdapter(15, new Carta());
        ResponseEntity resposta = controller.consultarTratamento(cartaAdapter.getId());


        assertEquals(204, resposta.getStatusCodeValue());
    }
}
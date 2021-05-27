package br.com.bandtec.AC3.scheduling;

import br.com.bandtec.AC3.adapter.CartaAdapter;
import br.com.bandtec.AC3.adapter.DeckAdapter;
import br.com.bandtec.AC3.controller.CartaController;
import br.com.bandtec.AC3.controller.DeckController;
import br.com.bandtec.AC3.interator.FilaObj;
import br.com.bandtec.AC3.interator.ListaObj;
import br.com.bandtec.AC3.repository.CartaRepository;
import br.com.bandtec.AC3.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulingService {

    @Autowired
    private CartaRepository cartaRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Scheduled( fixedRate = 5000)
    public void tratarRequisicaoDeck(){

        FilaObj<DeckAdapter> filaDeckRequisicao = DeckController.getFilaDeckRequisicao();

        ListaObj<DeckAdapter> listaDeckTratada = DeckController.getListaDeckTratada();

        if(!filaDeckRequisicao.isEmpty()){
            listaDeckTratada.adiciona(filaDeckRequisicao.peek());
            deckRepository.save(filaDeckRequisicao.poll().getDeck());

        } else {
            System.out.println("Sem tratamento decks pendentes");;
        }
    }

    @Scheduled( fixedRate = 6000)
    public void tratarRequisicaoCarta(){

        FilaObj<CartaAdapter> filaCartaRequisicao = CartaController.getFilaCartaRequisicao();

        ListaObj<CartaAdapter> listaCartaTratada = CartaController.getListaCartaTratada();

        if(!filaCartaRequisicao.isEmpty()){
            listaCartaTratada.adiciona(filaCartaRequisicao.peek());
            cartaRepository.save(filaCartaRequisicao.poll().getCarta());

        } else {
            System.out.println("Sem tratamentos cartas pendentes");;
        }
    }

}

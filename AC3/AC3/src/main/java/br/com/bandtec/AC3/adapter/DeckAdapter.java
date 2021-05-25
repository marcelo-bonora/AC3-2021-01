package br.com.bandtec.AC3.adapter;

import br.com.bandtec.AC3.model.Deck;

public class DeckAdapter {

    private Integer id;

    private Deck deck;

    public DeckAdapter(Integer id, Deck deck) {
        this.id = id;
        this.deck = deck;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }
}

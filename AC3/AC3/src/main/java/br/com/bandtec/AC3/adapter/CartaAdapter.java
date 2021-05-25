package br.com.bandtec.AC3.adapter;

import br.com.bandtec.AC3.model.Carta;

public class CartaAdapter {

    private Integer id;

    private Carta carta;

    public CartaAdapter(Integer id, Carta carta) {
        this.id = id;
        this.carta = carta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }
}

package br.com.bandtec.AC3.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Entity
public class Carta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCarta;


    @NotBlank
    private String nomeCarta;

    @NotBlank
    private String tipo;

    @PositiveOrZero
    private Double valorCarta;

    @ManyToOne
    private Deck deck;

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Integer getIdCarta() {
        return idCarta;
    }

    public void setIdCarta(Integer id) {
        this.idCarta = id;
    }

    public String getNomeCarta() {
        return nomeCarta;
    }

    public void setNomeCarta(String nome) {
        this.nomeCarta = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValorCarta() {
        return valorCarta;
    }

    public void setValorCarta(Double valor) {
        this.valorCarta = valor;
    }

    @Override
    public String toString() {
        return "Carta{" +
                "cod=" + idCarta +
                ", nome='" + nomeCarta + '\'' +
                ", tipo='" + tipo + '\'' +
                ", valor=" + valorCarta +
                '}';
    }
}

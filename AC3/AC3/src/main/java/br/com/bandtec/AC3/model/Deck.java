package br.com.bandtec.AC3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Entity
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDeck;

    @NotBlank
    private String nomeDeck;

    @PositiveOrZero
    private Integer qtdCartas;

    @PositiveOrZero
    private Double valorDeck;

    @OneToMany(mappedBy = "deck")
    @JsonIgnore
    private List<Carta> cartas;

    public Integer getIdDeck() {
        return idDeck;
    }

    public void setIdDeck(Integer idDeck) {
        this.idDeck = idDeck;
    }

    public String getNomeDeck() {
        return nomeDeck;
    }

    public void setNomeDeck(String nomeDeck) {
        this.nomeDeck = nomeDeck;
    }

    public Integer getQtdCartas() {
        return qtdCartas;
    }

    public void setQtdCartas(Integer qtdCartas) {
        this.qtdCartas = qtdCartas;
    }

    public Double getValorDeck() {
        return valorDeck;
    }

    public void setValorDeck(Double valorDeck) {
        this.valorDeck = valorDeck;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }
}

package br.com.bandtec.AC3.repository;

import br.com.bandtec.AC3.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<Deck, Integer> {
}

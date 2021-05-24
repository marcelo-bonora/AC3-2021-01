package br.com.bandtec.AC3.repository;

import br.com.bandtec.AC3.model.Carta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaRepository extends JpaRepository<Carta, Integer> {
}

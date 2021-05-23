package br.com.bandtec.AC3.Repository;

import br.com.bandtec.AC3.Model.Carta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaRepository extends JpaRepository<Carta, Integer> {
}

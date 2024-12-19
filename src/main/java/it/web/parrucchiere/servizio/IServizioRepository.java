package it.web.parrucchiere.servizio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface IServizioRepository extends JpaRepository<Servizio, Long> {

	@Query(value = "SELECT s.id, s.nome, s.prezzo FROM servizi s WHERE s.id IN (SELECT MIN(s2.id) FROM servizi s2 GROUP BY s2.nome )ORDER BY s.nome", nativeQuery = true)
	List<Servizio> getListaNomiServizi();
	
	List<Servizio> findAllByOrderByNomeAscPrezzoAsc();

}

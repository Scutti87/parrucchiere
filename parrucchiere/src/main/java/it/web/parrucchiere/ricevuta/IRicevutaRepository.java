package it.web.parrucchiere.ricevuta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.web.parrucchiere.appuntamento.Appuntamento;
import jakarta.transaction.Transactional;

@Repository
public interface IRicevutaRepository extends JpaRepository<Ricevuta, Long> {

	@Query(value = "SELECT r.id FROM ricevute r ORDER BY r.id DESC LIMIT 1", nativeQuery = true)
	long recuperaUltimoId();

	@Transactional
	@Modifying
	@Query(value = "ALTER TABLE ricevute AUTO_INCREMENT = :id", nativeQuery = true)
	void setAutoIncrement(@Param("id") long id);
	
	@Query(value = "SELECT * FROM ricevute r WHERE r.cliente_id = :id ORDER BY r.data", nativeQuery = true)
	List<Ricevuta> getListaRicevuteByCliente(@Param("id") long id);
	
	List<Ricevuta> findAllByOrderByDataAsc();

}

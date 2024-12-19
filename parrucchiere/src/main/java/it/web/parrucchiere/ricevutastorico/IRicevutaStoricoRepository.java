package it.web.parrucchiere.ricevutastorico;

import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IRicevutaStoricoRepository extends JpaRepository<RicevutaStorico, Long> {
	
	@Query(value = "SELECT * FROM ricevutestorico r WHERE r.cliente_id = :id ORDER BY r.data", nativeQuery = true)
	List<RicevutaStorico> getListaRicevuteByCliente(@Param("id") long id);
	
	List<RicevutaStorico> findAllByOrderByDataDesc();
	
	@Query(value = "SELECT r.id FROM ricevutestorico r ORDER BY r.id DESC LIMIT 1", nativeQuery = true)
	long recuperaUltimoId();
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO ricevutestorico (id, data, totale, cliente_id, operatore_id) VALUES (:id, :data, :totale, :cliente_id, :operatore_id)", nativeQuery = true)
	void aggiornaStorico(@Param("id") long id, @Param("data") Date data, @Param("totale") double totale, @Param("cliente_id") long clienteId,
			@Param("operatore_id") long operatoreId);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO servizi_ricevutestorico (ricevutastorico_id, servizio_id) VALUES (:ricevutastorico_id, :servizio_id)", nativeQuery = true)
	void aggiornaStoricoServizi(@Param("ricevutastorico_id") long ricevutastoricoid, @Param("servizio_id") long servizioId);

}

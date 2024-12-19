package it.web.parrucchiere.appuntamentostorico;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IAppuntamentoStoricoRepository extends JpaRepository<AppuntamentoStorico, Long> {

	@Query(value = "SELECT * FROM appuntamentistorico a WHERE a.cliente_id = :id ORDER BY a.data", nativeQuery = true)
	List<AppuntamentoStorico> getListaAppuntamentiStoricoByCliente(@Param("id") long id);

	List<AppuntamentoStorico> findAllByOrderByDataDescOraAsc();
	
	@Query(value = "SELECT a.id FROM appuntamentistorico a ORDER BY a.id DESC LIMIT 1", nativeQuery = true)
	long recuperaUltimoId();

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO appuntamentistorico (id, data, ora, cliente_id, operatore_id) VALUES (:id, :data, :ora, :cliente_id, :operatore_id)", nativeQuery = true)
	void aggiornaStorico(@Param("id") long id,  @Param("data") Date data, @Param("ora") Time ora, @Param("cliente_id") long clienteId,
			@Param("operatore_id") long operatoreId);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO servizi_appuntamentistorico (appuntamentostorico_id, servizio_id) VALUES (:appuntamentostorico_id, :servizio_id)", nativeQuery = true)
	void aggiornaStoricoServizi(@Param("appuntamentostorico_id") long appuntamentostoricoId, @Param("servizio_id") long servizioId);
}

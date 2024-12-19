package it.web.parrucchiere.appuntamento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface IAppuntamentoRepository extends JpaRepository<Appuntamento, Long> {

	@Query(value = "SELECT a.id FROM appuntamenti a ORDER BY a.id DESC LIMIT 1", nativeQuery = true)
	long recuperaUltimoId();

	@Transactional
	@Modifying
	@Query(value = "ALTER TABLE appuntamenti AUTO_INCREMENT = :id", nativeQuery = true)
	void setAutoIncrement(@Param("id") long id);
	
	@Query(value = "SELECT * FROM appuntamenti a WHERE a.cliente_id = :id ORDER BY a.data", nativeQuery = true)
	List<Appuntamento> getListaAppuntamentiByCliente(@Param("id") long id);
	
	 List<Appuntamento> findAllByOrderByDataAscOraAsc();

}

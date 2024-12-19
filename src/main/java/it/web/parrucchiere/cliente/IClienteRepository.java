package it.web.parrucchiere.cliente;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Long> {
	
	@Query(value = "SELECT c.id FROM clienti c ORDER BY c.id DESC LIMIT 1", nativeQuery = true)
	long recuperaUltimoId();
	
	@Transactional
	@Modifying
	@Query(value = "ALTER TABLE clienti AUTO_INCREMENT = :id", nativeQuery = true)
	void setAutoIncrement(@Param("id") long id);
	
	List<Cliente> findByNomeStartingWith(String iniziali);

}

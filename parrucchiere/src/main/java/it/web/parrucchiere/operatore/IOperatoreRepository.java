package it.web.parrucchiere.operatore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IOperatoreRepository extends JpaRepository<Operatore, Long> {

	List<Operatore> findByNomeStartingWith(String iniziali);
}

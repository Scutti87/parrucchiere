package it.web.parrucchiere.operatore;

import java.util.List;

public interface IOperatoreService {

	OperatoreDto getOperatoreById(long id);

	List<OperatoreDto> getListaOperatori();
	
	List<OperatoreDto> getListaOperatoriByIniziali(String iniziali);

}

package it.web.parrucchiere.servizio;

import java.util.List;

public interface IServizioService {

	ServizioDto getServizioById(long id);

	List<ServizioDto> getListaServizi();
	
	List<ServizioDto> getListaNomiServizi();

}

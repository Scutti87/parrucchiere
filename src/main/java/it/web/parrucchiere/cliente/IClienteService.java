package it.web.parrucchiere.cliente;

import java.util.List;

public interface IClienteService {
	
	ClienteDto inserisci(ClienteDto c);

	ClienteDto aggiorna(ClienteDto c);
	
	boolean elimina(long id);
	
	ClienteDto getClienteById(long id);
	
	List<ClienteDto> getListaClienti();
	
	List<ClienteDto> getListaClientiByIniziali(String iniziali);
}

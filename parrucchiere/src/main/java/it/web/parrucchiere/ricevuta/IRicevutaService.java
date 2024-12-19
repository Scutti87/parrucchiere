package it.web.parrucchiere.ricevuta;

import java.util.List;

public interface IRicevutaService {

	RicevutaDto inserisci(RicevutaDto r);

	RicevutaDto aggiorna(RicevutaDto r);

	boolean elimina(long id);

	RicevutaDto getRicevutaById(long id);

	List<RicevutaDto> getListaRicevute();

	List<RicevutaDto> getListaRicevuteByCliente(long id);

}

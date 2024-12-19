package it.web.parrucchiere.ricevutastorico;

import java.util.List;

public interface IRicevutaStoricoService {

	void aggiornaStorico(RicevutaStoricoDto a);

	void aggiornaStoricoServizi(long ricid, long servid);

	RicevutaStoricoDto getRicevutaStoricoById(long id);

	List<RicevutaStoricoDto> getListaRicevuteStorico();

	List<RicevutaStoricoDto> getListaRicevuteStoricoByCliente(long id);

}

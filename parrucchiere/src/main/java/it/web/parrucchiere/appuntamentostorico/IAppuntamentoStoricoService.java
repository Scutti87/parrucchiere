package it.web.parrucchiere.appuntamentostorico;

import java.util.List;


public interface IAppuntamentoStoricoService {
	
	void aggiornaStorico(AppuntamentoStoricoDto a);
	
	void aggiornaStoricoServizi(long appid, long servid);

	AppuntamentoStoricoDto getAppuntamentoStoricoById(long id);

	List<AppuntamentoStoricoDto> getListaAppuntamentiStorico();

	List<AppuntamentoStoricoDto> getListaAppuntamentiStoricoByCliente(long id);

}

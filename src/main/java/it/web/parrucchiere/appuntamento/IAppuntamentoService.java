package it.web.parrucchiere.appuntamento;

import java.util.List;

public interface IAppuntamentoService {

	AppuntamentoDto inserisci(AppuntamentoDto a);

	AppuntamentoDto aggiorna(AppuntamentoDto a);

	boolean elimina(long id);

	AppuntamentoDto getAppuntamentoById(long id);

	List<AppuntamentoDto> getListaAppuntamenti();

	List<AppuntamentoDto> getListaAppuntamentiByCliente(long id);

}

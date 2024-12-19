package it.web.parrucchiere.appuntamentostorico;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import it.web.parrucchiere.cliente.ClienteDto;
import it.web.parrucchiere.operatore.OperatoreDto;
import it.web.parrucchiere.servizio.ServizioDto;
import lombok.Data;

@Data
public class AppuntamentoStoricoDto {

	private long id;
	private Date data;
	private Time ora;
	private ClienteDto cliente;
	private OperatoreDto operatore;
	private List<ServizioDto> servizi;
	private String orario;



}

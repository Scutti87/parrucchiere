package it.web.parrucchiere.ricevutastorico;

import java.sql.Date;
import java.util.List;
import it.web.parrucchiere.cliente.ClienteDto;
import it.web.parrucchiere.operatore.OperatoreDto;
import it.web.parrucchiere.servizio.ServizioDto;
import lombok.Data;

@Data
public class RicevutaStoricoDto {

	private long id;
	private double totale;
	private Date data;
	private ClienteDto cliente;
	private OperatoreDto operatore;
	private List<ServizioDto> servizi;

}

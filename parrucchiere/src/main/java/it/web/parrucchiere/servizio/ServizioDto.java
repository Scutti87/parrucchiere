package it.web.parrucchiere.servizio;

import lombok.Data;

@Data
public class ServizioDto {

	private long id;
	private String nome;
	private double prezzo;

	@Override
	public String toString() {
		return nome ;
	}

}

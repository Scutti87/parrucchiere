package it.web.parrucchiere.persona;

import java.sql.Date;
import java.time.LocalDate;

import lombok.Data;

@Data
public class PersonaDto {

	private String nome;
	private String cognome;
	private Date ddn = Date.valueOf(LocalDate.now());
	private String email;
	private String telefono;

}

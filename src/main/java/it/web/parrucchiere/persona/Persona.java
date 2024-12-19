package it.web.parrucchiere.persona;

import java.io.Serializable;
import java.sql.Date;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;


@Data
@MappedSuperclass
public class Persona implements Serializable{
	private static final long serialVersionUID = 1L;

	private String nome;
	private String cognome;
	private Date ddn;
	private String email;
	private String telefono;

}

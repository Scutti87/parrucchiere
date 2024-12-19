package it.web.parrucchiere.cliente;

import java.sql.Date;
import it.web.parrucchiere.persona.PersonaDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClienteDto extends PersonaDto {

	private long id;
	private String scheda;
	private Date dataRegistrazione;

	public void trimCampi() {
		if (getNome() != null) {
			setNome(getNome().trim());
		}
		if (getCognome() != null) {
			setCognome(getCognome().trim());
		}
		if (getEmail() != null) {
			setEmail(getEmail().trim());
		}
		if (getTelefono() != null) {
			setTelefono(getTelefono().trim());
		}
	}



}

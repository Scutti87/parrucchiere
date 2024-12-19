package it.web.parrucchiere.operatore;

import java.sql.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import it.web.parrucchiere.persona.PersonaDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OperatoreDto extends PersonaDto {

	
	private long id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Europe/Rome")
	private Date dataAssunzione;
	private int livello;


}

package it.web.parrucchiere.cliente;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import it.web.parrucchiere.appuntamento.Appuntamento;
import it.web.parrucchiere.persona.Persona;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "clienti")
public class Cliente extends Persona implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Date dataRegistrazione;
	private String scheda;
	@OneToMany(mappedBy = "cliente")
	private List<Appuntamento> appuntamenti;

}

package it.web.parrucchiere.ricevuta;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import it.web.parrucchiere.cliente.Cliente;
import it.web.parrucchiere.operatore.Operatore;
import it.web.parrucchiere.servizio.Servizio;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ricevute")
public class Ricevuta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Date data;
	private double totale;
	@ManyToOne
	private Cliente cliente;
	@ManyToOne
	private Operatore operatore;
	@ManyToMany
	@JoinTable(name = "servizi_ricevute", joinColumns = @JoinColumn(name = "ricevuta_id"), inverseJoinColumns = @JoinColumn(name = "servizio_id"))
	private List<Servizio> servizi;
}

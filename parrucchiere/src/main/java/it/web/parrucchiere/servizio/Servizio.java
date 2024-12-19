package it.web.parrucchiere.servizio;

import java.io.Serializable;
import java.util.List;
import it.web.parrucchiere.appuntamento.Appuntamento;
import it.web.parrucchiere.appuntamentostorico.AppuntamentoStorico;
import it.web.parrucchiere.ricevuta.Ricevuta;
import it.web.parrucchiere.ricevutastorico.RicevutaStorico;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "servizi")
public class Servizio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String nome;
	private double prezzo;
	@ManyToMany
	@JoinTable(name = "servizi_appuntamenti", joinColumns = @JoinColumn(name = "servizio_id"), inverseJoinColumns = @JoinColumn(name = "appuntamento_id"))
	List<Appuntamento> appuntamenti;
	@ManyToMany
	@JoinTable(name = "servizi_appuntamentistorico", joinColumns = @JoinColumn(name = "servizio_id"), inverseJoinColumns = @JoinColumn(name = "appuntamentostorico_id"))
	List<AppuntamentoStorico> appuntamentistorico;
	@ManyToMany
	@JoinTable(name = "servizi_ricevute", joinColumns = @JoinColumn(name = "servizio_id"), inverseJoinColumns = @JoinColumn(name = "ricevuta_id"))
	List<Ricevuta> ricevute;
	@ManyToMany
	@JoinTable(name = "servizi_ricevutestorico", joinColumns = @JoinColumn(name = "servizio_id"), inverseJoinColumns = @JoinColumn(name = "ricevutastorico_id"))
	List<RicevutaStorico> ricevutestorico;
	
}

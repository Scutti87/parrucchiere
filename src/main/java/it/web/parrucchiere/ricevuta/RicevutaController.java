package it.web.parrucchiere.ricevuta;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.web.parrucchiere.cliente.ClienteDto;
import it.web.parrucchiere.cliente.IClienteService;
import it.web.parrucchiere.exception.BusinessException;
import it.web.parrucchiere.operatore.IOperatoreService;
import it.web.parrucchiere.servizio.IServizioService;

@Controller
@RequestMapping("ricevuta")
public class RicevutaController {

	private final IClienteService cs;
	private final IServizioService ss;
	private final IOperatoreService os;
	private final IRicevutaService rs;

	@Autowired
	public RicevutaController(IRicevutaService rs, IClienteService cs, IServizioService ss, IOperatoreService os) {

		this.cs = cs;
		this.ss = ss;
		this.os = os;
		this.rs = rs;
	}

	@GetMapping("/getListaRicevute")
	public String getListaRicevute(Model m) {

		try {
			List<RicevutaDto> lista = rs.getListaRicevute();
			m.addAttribute("listaRicevute", lista);

		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		return "listaRicevute";
	}

	@GetMapping("/inserisciRicevutaForm")
	public String inserisciRicevutaForm(Model m) {

		RicevutaDto r = new RicevutaDto();
		m.addAttribute("venga", "inserisci");
		m.addAttribute("ricevuta", r);
		m.addAttribute("serviziDisponibili", ss.getListaServizi());

		return "inserisciRicevuta";
	}

	@GetMapping("/aggiornaRicevutaForm")
	public String aggiornaRicevutaForm(Model m, @RequestParam("id") long id) {

		try {
			RicevutaDto r = rs.getRicevutaById(id);
			m.addAttribute("venga", "aggiorna");
			m.addAttribute("ricevuta", r);
			m.addAttribute("serviziDisponibili", ss.getListaServizi());
			m.addAttribute("servizi", r.getServizi());

			if (r.getCliente() != null) {
				m.addAttribute("clienteId", r.getCliente().getId());
			}
			if (r.getOperatore() != null) {
				m.addAttribute("operatoreId", r.getOperatore().getId());
			}
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
			m.addAttribute("vengo", "admin");
			return "listaRicevute";
		}

		return "inserisciRicevuta";
	}

	@PostMapping("/inserisciRicevuta")
	public String inserisciAppuntamento(@ModelAttribute RicevutaDto r, Model m, @RequestParam("venga") String vengo,
			@RequestParam("clienteId1") long idCliente, @RequestParam("operatoreId") long idOperatore) {

		try {
			r.setOperatore(os.getOperatoreById(idOperatore));
			r.setCliente(cs.getClienteById(idCliente));
			RicevutaDto ricevuta = null;
			if (vengo.equals("inserisci")) {
				ricevuta = rs.inserisci(r);
			} else {
				ricevuta = rs.aggiorna(r);
			}
			List<RicevutaDto> lista = new ArrayList<RicevutaDto>();
			lista.add(ricevuta);
			m.addAttribute("listaRicevute", lista);
			m.addAttribute("mex", "Operazione avvenuta con successo");

		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		m.addAttribute("vengo", "admin");

		return "listaRicevute";
	}

	@GetMapping("/eliminaRicevuta")
	public String eliminaRicevuta(@RequestParam("id") long id, Model m) {

		try {
			boolean eliminato = rs.elimina(id);
			if (eliminato) {
				m.addAttribute("mex", "Operazione avvenuta con successo");
				m.addAttribute("listaRicevute", rs.getListaRicevute());
			}
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		m.addAttribute("vengo", "admin");

		return "listaRicevute";
	}

	@GetMapping("/cercaRicevuta")
	public String cercaRicevuta(@RequestParam("clienteIdRic") long id, Model m) {

		try {
			ClienteDto c = cs.getClienteById(id);
			List<RicevutaDto> lista = rs.getListaRicevuteByCliente(c.getId());

			m.addAttribute("listaRicevute", lista);
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}

		return "listaRicevute";
	}

}

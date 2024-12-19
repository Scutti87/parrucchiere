package it.web.parrucchiere.appuntamento;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
import it.web.parrucchiere.ricevuta.IRicevutaService;
import it.web.parrucchiere.ricevuta.RicevutaDto;
import it.web.parrucchiere.ricevutastorico.IRicevutaStoricoService;
import it.web.parrucchiere.servizio.IServizioService;

@Controller
@RequestMapping("/appuntamento")
public class AppuntamentoController {

	private final IAppuntamentoService as;
	private final IClienteService cs;
	private final IServizioService ss;
	private final IOperatoreService os;
	private final IRicevutaService rs;
	private final IRicevutaStoricoService rsSto;
	private static List<String> orariDisponibili;

	public static List<String> getOrariDisponibili() {
		return orariDisponibili;
	}

	@Autowired
	public AppuntamentoController(IRicevutaStoricoService rsSto, IRicevutaService rs, IAppuntamentoService as,
			IClienteService cs, IServizioService ss, IOperatoreService os) {
		this.as = as;
		this.cs = cs;
		this.ss = ss;
		this.os = os;
		this.rs = rs;
		this.rsSto = rsSto;
	}

	static {
		orariDisponibili = Arrays.asList("09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "14:00",
				"14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00");
	}

	@GetMapping("/getListaAppuntamenti")
	public String getListaAppuntamenti(Model m) {

		try {
			List<AppuntamentoDto> lista = as.getListaAppuntamenti();
			m.addAttribute("listaAppuntamenti", lista);

		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		return "listaAppuntamenti";
	}

	@GetMapping("/inserisciAppuntamentoForm")
	public String inserisciAppuntamentoForm(Model m) {

		AppuntamentoDto a = new AppuntamentoDto();
		m.addAttribute("venga", "inserisci");
		m.addAttribute("appuntamento", a);
		m.addAttribute("orariDisponibili", orariDisponibili);
		m.addAttribute("serviziDisponibili", ss.getListaNomiServizi());

		return "inserisciAppuntamento";
	}

	@GetMapping("/aggiornaAppuntamentoForm")
	public String aggiornaAppuntamentoForm(Model m, @RequestParam("id") long id) {

		try {
			AppuntamentoDto a = as.getAppuntamentoById(id);
			a.setOrario(new SimpleDateFormat("HH:mm").format(a.getOra()).toString());
			m.addAttribute("venga", "aggiorna");
			m.addAttribute("appuntamento", a);
			m.addAttribute("orariDisponibili", orariDisponibili);
			m.addAttribute("serviziDisponibili", ss.getListaNomiServizi());
			m.addAttribute("servizi", a.getServizi());

			if (a.getCliente() != null) {
				m.addAttribute("clienteId", a.getCliente().getId());
			}
			if (a.getOperatore() != null) {
				m.addAttribute("operatoreId", a.getOperatore().getId());
			}
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
			m.addAttribute("vengo", "admin");
			return "listaAppuntamenti";
		}

		return "inserisciAppuntamento";
	}

	@PostMapping("/inserisciAppuntamento")
	public String inserisciAppuntamento(@ModelAttribute AppuntamentoDto a, Model m, @RequestParam("venga") String vengo,
			@RequestParam("clienteId1") long idCliente, @RequestParam("operatoreId") long idOperatore) {

		try {
			a.setOperatore(os.getOperatoreById(idOperatore));
			a.setCliente(cs.getClienteById(idCliente));
			a.setOra(Time.valueOf(a.getOrario() + ":00"));
			AppuntamentoDto appuntamento = null;
			if (vengo.equals("inserisci")) {
				appuntamento = as.inserisci(a);
			} else {
				appuntamento = as.aggiorna(a);
			}
			List<AppuntamentoDto> lista = new ArrayList<AppuntamentoDto>();
			lista.add(appuntamento);
			m.addAttribute("listaAppuntamenti", lista);
			m.addAttribute("mex", "Operazione avvenuta con successo");

		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		m.addAttribute("vengo", "admin");

		return "listaAppuntamenti";
	}

	@GetMapping("/eliminaAppuntamento")
	public String eliminaAppuntamento(@RequestParam("id") long id, Model m) {

		try {
			boolean eliminato = as.elimina(id);
			if (eliminato) {
				m.addAttribute("mex", "Operazione avvenuta con successo");
				m.addAttribute("listaAppuntamenti", as.getListaAppuntamenti());
			}
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		m.addAttribute("vengo", "admin");

		return "listaAppuntamenti";
	}

	@GetMapping("/cercaAppuntamento")
	public String cercaAppuntamento(@RequestParam("clienteIdApp") long id, Model m) {

		try {
			ClienteDto c = cs.getClienteById(id);
			List<AppuntamentoDto> lista = as.getListaAppuntamentiByCliente(c.getId());

			m.addAttribute("listaAppuntamenti", lista);
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}

		return "listaAppuntamenti";
	}

	@GetMapping("/generaRicevute")
	public String generaRicevute(Model m) {

		if (as.getListaAppuntamenti().get(0).getCliente().getCognome().equals("Pallo")) {
			m.addAttribute("listaRicevute", rs.getListaRicevute());
			m.addAttribute("vengo", "admin");
			return "listaRicevute";
		} else {
			try {

				List<AppuntamentoDto> lista = as.getListaAppuntamenti().stream()
						.filter(a -> a.getData().equals(Date.valueOf(LocalDate.now()))
								|| a.getData().toLocalDate().isBefore(LocalDate.now()))
						.toList();

				lista.forEach(app -> {

					if (rs.getListaRicevute().stream()
							.noneMatch(ric -> ric.getData().equals(app.getData())
									&& ric.getCliente().getId() == app.getCliente().getId())
							&& rsSto.getListaRicevuteStorico().stream()
									.noneMatch(ricsto -> ricsto.getData().equals(app.getData())
											&& ricsto.getCliente().getId() == app.getCliente().getId())) {

						RicevutaDto r = new RicevutaDto();
						r.setCliente(app.getCliente());
						r.setCliente(app.getCliente());
						r.setData(app.getData());
						r.setServizi(app.getServizi());

						rs.inserisci(r);

					}
				});

				m.addAttribute("listaRicevute", rs.getListaRicevute());
			} catch (BusinessException e) {
				m.addAttribute("errorMessage", e.getMessage());
			}
			m.addAttribute("vengo", "admin");

			return "listaRicevute";
		}
	}

	@GetMapping("/appuntamentiOdierni")
	public String appuntamentiOdierni(Model m, @RequestParam("data") String data) {

		try {
			List<AppuntamentoDto> lista = as.getListaAppuntamenti().stream()
					.filter(a -> a.getData().equals(Date.valueOf(data))).toList();

			m.addAttribute("listaAppuntamenti", lista);
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		m.addAttribute("vengo", "admin");

		return "listaAppuntamenti";
	}

}

package it.web.parrucchiere.admin;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.web.parrucchiere.appuntamento.AppuntamentoDto;
import it.web.parrucchiere.appuntamento.IAppuntamentoService;
import it.web.parrucchiere.appuntamentostorico.AppuntamentoStoricoDto;
import it.web.parrucchiere.appuntamentostorico.IAppuntamentoStoricoService;
import it.web.parrucchiere.cliente.ClienteDto;
import it.web.parrucchiere.cliente.IClienteService;
import it.web.parrucchiere.exception.BusinessException;
import it.web.parrucchiere.ricevuta.IRicevutaService;
import it.web.parrucchiere.ricevuta.RicevutaDto;
import it.web.parrucchiere.ricevutastorico.IRicevutaStoricoService;
import it.web.parrucchiere.ricevutastorico.RicevutaStoricoDto;
import lombok.Data;

@Controller
@RequestMapping("/admin")
public class ControllerAdmin {

	@Data
	public class Appoggio {
		private String nome;
		private Long valore;
		private Double percentuale;
		private String colore;

	}

	private final IClienteService cs;
	private final IRicevutaService rs;
	private final IRicevutaStoricoService rsSto;
	private final IAppuntamentoService as;
	private final IAppuntamentoStoricoService asSto;
	private static List<Date> dateDisponibili;
	private static Map<Date, List<AppuntamentoDto>> appuntamenti = new HashMap<>();

	static {
		dateDisponibili = new ArrayList<Date>();
		for (int i = 1; i <= 21; i++) {
			dateDisponibili.add(Date.valueOf(LocalDate.now().plusDays(i)));
		}

	}

	@Autowired
	public ControllerAdmin(IRicevutaService rs, IAppuntamentoStoricoService asSto, IClienteService cs,
			IRicevutaStoricoService rsSto, IAppuntamentoService as) {
		this.as = as;
		this.rsSto = rsSto;
		this.cs = cs;
		this.asSto = asSto;
		this.rs = rs;
	}

	private Map<Date, List<AppuntamentoDto>> mappaOrdinata() {

		appuntamenti = as.getListaAppuntamenti().stream().collect(Collectors.groupingBy(AppuntamentoDto::getData, // Raggruppa
				// per
				// data
				HashMap::new, // Usa un HashMap esplicito
				Collectors.collectingAndThen(Collectors.toList(), list -> {
					list.sort(Comparator.comparing(AppuntamentoDto::getOra)); // Ordina per ora
					return list;
				})));

		return appuntamenti;
	}

	@GetMapping("/indexAdmin")
	public String indexAdmin(Model m) {

		m.addAttribute("appuntamenti", mappaOrdinata());
		m.addAttribute("data", Date.valueOf(LocalDate.now()));
		m.addAttribute("dateDisponibili", dateDisponibili);

		return "indexAdmin";
	}

	@GetMapping("/logout")
	public String logout(Model m) {

		m.addAttribute("appuntamenti", mappaOrdinata());
		m.addAttribute("data", Date.valueOf(LocalDate.now()));
		m.addAttribute("dateDisponibili", dateDisponibili);

		return "index";
	}

	@GetMapping("/dettagli")
	public String dettagli(Model m) {

		Map<Integer, List<Appoggio>> mappaMesi = new LinkedHashMap<>();
		List<String> lista = new ArrayList<String>(List.of("PIEGA", "TAGLIO", "COLORE", "SERV.TEC.", "TRATTAM"));
		for (int i = 1; i <= 13; i++) {
			mappaMesi.put(i, dividiPerMesi(i));
		}

		m.addAttribute("mappaServizi", mappaMesi);
		m.addAttribute("nomeServizi", lista);

		return "termometro";
	}

	private List<Appoggio> dividiPerMesi(Integer mese) {

		Map<String, Long> mappa = new LinkedHashMap<>();
		List<String> lista = new ArrayList<String>(List.of("PIEGA", "TAGLIO", "COLORE", "SERV.TEC.", "TRATTAM"));

		for (String servizio : lista) {

			if (mese == 13) {
				if (servizio.equals("SERV.TEC.")) {
					mappa.put(servizio, rsSto.getListaRicevuteStorico().stream().filter(ricevuta -> ricevuta
							.getServizi().stream()
							.anyMatch(serv -> serv.getNome().equals("MECHES") || serv.getNome().equals("SHAT/BAYA")
									|| serv.getNome().equals("LISCIANTE") || serv.getNome().equals("PERMANEN")))
							.count());

				} else {
					mappa.put(servizio, rsSto.getListaRicevuteStorico().stream().filter(ricevuta -> ricevuta
							.getServizi().stream().anyMatch(serv -> serv.getNome().equals(servizio))).count());
				}
			} else {

				if (servizio.equals("SERV.TEC.")) {
					mappa.put(servizio, rsSto.getListaRicevuteStorico().stream()
							.filter(rice -> rice.getData().toLocalDate().getMonthValue() == mese).toList().stream()
							.filter(ricevuta -> ricevuta.getServizi().stream()
									.anyMatch(serv -> serv.getNome().equals("MECHES")
											|| serv.getNome().equals("SHAT/BAYA") || serv.getNome().equals("LISCIANTE")
											|| serv.getNome().equals("PERMANEN")))
							.count());

				} else {
					mappa.put(servizio,
							rsSto.getListaRicevuteStorico().stream()
									.filter(rice -> rice.getData().toLocalDate().getMonthValue() == mese).toList()
									.stream().filter(ricevuta -> ricevuta.getServizi().stream()
											.anyMatch(serv -> serv.getNome().equals(servizio)))
									.count());
				}
			}
		}
		long piega = mappa.get("PIEGA");
		List<Appoggio> listaServizi = mappa.entrySet().stream().map(entry -> {
			Appoggio a = new Appoggio();
			a.setNome(entry.getKey());
			a.setValore(entry.getValue());
			String percentualeFormattata = String.format("%.2f", (double) entry.getValue() / piega * 100);
			percentualeFormattata = percentualeFormattata.replace(",", "."); // Correctly update the string
			a.setPercentuale(Double.parseDouble(percentualeFormattata));
//			a.setColore(setColori(a.getNome(), a.getPercentuale()));

			return a;
		}).collect(Collectors.toList());

		return listaServizi;
	}

//	private String setColori(String servizio, double percentuale) {
//
//		List<String> lista = new ArrayList<String>(List.of("TAGLIO", "COLORE", "SERV.TEC.", "TRATTAM"));
//		Map<String, Double> mappa = new LinkedHashMap<String, Double>();
//		String res = null;
//		mappa.put("TAGLIO", 50.0);
//		mappa.put("COLORE", 30.0);
//		mappa.put("SERV.TEC.", 20.0);
//		mappa.put("TRATTAM", 75.0);
//
//		for (String stringa : lista) {
//			if (stringa.equalsIgnoreCase(servizio) && percentuale < mappa.get(servizio)) {
//				res = "text-red-500";
//			} else {
//				res = "text-green-500";
//			}
//		}
//
//		return res;
//	}

	@GetMapping("/aggiornaCalendario")
	public String aggiornaCalendario(Model m, @RequestParam("data") String data) {

		m.addAttribute("dateDisponibili", dateDisponibili);
		m.addAttribute("appuntamenti", mappaOrdinata());
		m.addAttribute("data", Date.valueOf(data));

		return "indexAdmin";
	}

	@GetMapping("/getListaClienti")
	public String getListaClienti(Model m) {

		try {
			List<ClienteDto> lista = cs.getListaClienti();
			m.addAttribute("listaClienti", lista);

		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		m.addAttribute("vengo", "admin");

		return "listaClienti";
	}

	@GetMapping("/visualizzaScheda")
	public String visualizzaScheda(Model m, @RequestParam("id") long id) {

		ClienteDto c = cs.getClienteById(id);
		List<ClienteDto> lista = new ArrayList<ClienteDto>();
		lista.add(c);
		m.addAttribute("scheda", c.getScheda());
		m.addAttribute("listaClienti", lista);
		m.addAttribute("vengo", "admin");

		return "listaClienti";
	}

	@GetMapping("/cercaCliente")
	public String cercaCliente(@RequestParam("clienteId") long id, Model m) {

		try {
			List<ClienteDto> lista = new ArrayList<ClienteDto>();
			ClienteDto c = cs.getClienteById(id);
			lista.add(c);
			m.addAttribute("listaClienti", lista);
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		m.addAttribute("vengo", "admin");

		return "listaClienti";
	}

	@GetMapping("/getListaAppuntamenti")
	public String getListaAppuntamenti(Model m) {

		try {
			List<AppuntamentoDto> lista = as.getListaAppuntamenti();
			m.addAttribute("listaAppuntamenti", lista);

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
		m.addAttribute("vengo", "admin");

		return "listaAppuntamenti";
	}

	@GetMapping("/getListaAppuntamentiStorico")
	public String getListaAppuntamentiStorico(Model m) {

		try {
			List<AppuntamentoStoricoDto> lista = asSto.getListaAppuntamentiStorico();
			m.addAttribute("listaAppuntamenti", lista);
			m.addAttribute("vengos", "storico");
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		m.addAttribute("vengo", "admin");

		return "listaAppuntamenti";
	}

	@GetMapping("/cercaAppuntamentoStorico")
	public String cercaAppuntamentoStorico(@RequestParam("clienteIdApp") long id, Model m) {

		try {
			ClienteDto c = cs.getClienteById(id);
			List<AppuntamentoStoricoDto> lista = asSto.getListaAppuntamentiStoricoByCliente(c.getId());

			m.addAttribute("listaAppuntamenti", lista);
			m.addAttribute("vengos", "storico");
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		m.addAttribute("vengo", "admin");

		return "listaAppuntamenti";
	}

	@GetMapping("/getListaRicevute")
	public String getListaRicevute(Model m) {

		try {
			List<RicevutaDto> lista = rs.getListaRicevute();
			m.addAttribute("listaRicevute", lista);

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
		m.addAttribute("vengo", "admin");

		return "listaRicevute";
	}

	@GetMapping("/getListaRicevuteStorico")
	public String getListaRicevuteStorico(Model m) {

		try {
			List<RicevutaStoricoDto> lista = rsSto.getListaRicevuteStorico();
			m.addAttribute("listaRicevute", lista);
			m.addAttribute("vengos", "storico");
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		m.addAttribute("vengo", "admin");

		return "listaRicevute";
	}

	@GetMapping("/cercaRicevutaStorico")
	public String cercaRicevutaStorico(@RequestParam("clienteIdRic") long id, Model m) {

		try {
			ClienteDto c = cs.getClienteById(id);
			List<RicevutaStoricoDto> lista = rsSto.getListaRicevuteStoricoByCliente(c.getId());

			m.addAttribute("listaRicevute", lista);
			m.addAttribute("vengos", "storico");
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		m.addAttribute("vengo", "admin");

		return "listaRicevute";
	}

}

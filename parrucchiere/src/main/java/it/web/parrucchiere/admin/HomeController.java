package it.web.parrucchiere.admin;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.web.parrucchiere.appuntamento.AppuntamentoDto;
import it.web.parrucchiere.appuntamento.IAppuntamentoService;

@Controller
public class HomeController {

	private final IAppuntamentoService as;
	private static List<Date> dateDisponibili;
	private static Map<Date, List<AppuntamentoDto>> appuntamenti = new HashMap<>();

	public HomeController(IAppuntamentoService as) {
		this.as = as;
	}

	static {
		dateDisponibili = new ArrayList<Date>();
		for (int i = 1; i <= 21; i++) {
			dateDisponibili.add(Date.valueOf(LocalDate.now().plusDays(i)));
		}

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

	@GetMapping("/")
	public String index(Model m) {

		m.addAttribute("appuntamenti", mappaOrdinata());
		m.addAttribute("data", Date.valueOf(LocalDate.now()));
		m.addAttribute("dateDisponibili", dateDisponibili);

		return "index";
	}

	@GetMapping("/aggiornaCalendario")
	public String aggiornaCalendario(Model m, @RequestParam("data") String data) {

		m.addAttribute("dateDisponibili", dateDisponibili);
		m.addAttribute("appuntamenti", mappaOrdinata());
		m.addAttribute("data", Date.valueOf(data));
		
		return "index";
	}

}

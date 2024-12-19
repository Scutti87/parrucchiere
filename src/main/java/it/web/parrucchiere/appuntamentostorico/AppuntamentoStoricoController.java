package it.web.parrucchiere.appuntamentostorico;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.web.parrucchiere.appuntamento.AppuntamentoDto;
import it.web.parrucchiere.appuntamento.IAppuntamentoService;
import it.web.parrucchiere.cliente.ClienteDto;
import it.web.parrucchiere.cliente.IClienteService;
import it.web.parrucchiere.exception.BusinessException;

@Controller
@RequestMapping("/appuntamentoStorico")
public class AppuntamentoStoricoController {

	private final IAppuntamentoStoricoService as;
	private final IClienteService cs;
	private final IAppuntamentoService appservice;

	@Autowired
	public AppuntamentoStoricoController(IAppuntamentoStoricoService as, IClienteService cs,
			IAppuntamentoService apps) {
		this.as = as;
		this.cs = cs;
		this.appservice = apps;
	}

	@GetMapping("/getListaAppuntamentiStorico")
	public String getListaAppuntamentiStorico(Model m) {

		try {
			List<AppuntamentoStoricoDto> lista = as.getListaAppuntamentiStorico();
			m.addAttribute("listaAppuntamenti", lista);
			m.addAttribute("vengos", "storico");
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		return "listaAppuntamenti";
	}

	@GetMapping("/cercaAppuntamentoStorico")
	public String cercaAppuntamentoStorico(@RequestParam("clienteIdApp") long id, Model m) {

		try {
			ClienteDto c = cs.getClienteById(id);
			List<AppuntamentoStoricoDto> lista = as.getListaAppuntamentiStoricoByCliente(c.getId());

			m.addAttribute("listaAppuntamenti", lista);
			m.addAttribute("vengos", "storico");
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}

		return "listaAppuntamenti";
	}

	@Transactional
	@GetMapping("/aggiornaStorico")
	public String aggiornaStorico(Model m) {
		try {
			List<AppuntamentoDto> listaApp = appservice.getListaAppuntamenti().stream()
					.filter(a -> a.getData().toLocalDate().isBefore(LocalDate.now())).toList();

			if (!listaApp.isEmpty()) {

				for (AppuntamentoDto app : listaApp) {

					AppuntamentoStoricoDto a = trasforma(app);
					as.aggiornaStorico(a);
					a.getServizi().forEach(s -> as.aggiornaStoricoServizi(a.getId(), s.getId()));

					appservice.elimina(app.getId());
				}
			}

			m.addAttribute("listaAppuntamenti", appservice.getListaAppuntamenti());

		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		m.addAttribute("vengo", "admin");

		return "listaAppuntamenti";
	}

	private AppuntamentoStoricoDto trasforma(AppuntamentoDto a) {
		AppuntamentoStoricoDto aps = new AppuntamentoStoricoDto();
		aps.setCliente(a.getCliente());
		aps.setData(a.getData());
		aps.setId(a.getId());
		aps.setOperatore(a.getOperatore());
		aps.setOra(a.getOra());
		aps.setOrario(a.getOrario());
		aps.setServizi(a.getServizi());
		return aps;
	}

}

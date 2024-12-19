package it.web.parrucchiere.cliente;

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
import it.web.parrucchiere.exception.BusinessException;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

	private final IClienteService cs;

	@Autowired
	public ClienteController(IClienteService cs) {
		this.cs = cs;
	}

//	@GetMapping("/getCliente")
//	public ClienteDto getClienteById(@RequestParam("id") long id) {
//
//		return cs.getClienteById(id);
//	}

	@GetMapping("/getListaClienti")
	public String getListaClienti(Model m) {

		try {
			List<ClienteDto> lista = cs.getListaClienti();
			m.addAttribute("listaClienti", lista);

		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}

		return "listaClienti";
	}

	@GetMapping("/inserisciClienteForm")
	public String inserisciClienteForm(Model m) {

		ClienteDto c = new ClienteDto();
		m.addAttribute("vengo", "inserisci");
		m.addAttribute("cliente", c);

		return "inserisciCliente";
	}

	@GetMapping("/aggiornaClienteForm")
	public String aggiornaClienteForm(Model m, @RequestParam("id") long id) {

		ClienteDto c = cs.getClienteById(id);
		m.addAttribute("vengo", "aggiorna");
		m.addAttribute("cliente", c);

		return "inserisciCliente";
	}

	@PostMapping("/inserisciCliente")
	public String inserisciCliente(@ModelAttribute ClienteDto c, Model m, @RequestParam("vengo") String vengo) {

		try {
			ClienteDto cliente = null;
			if (vengo.equals("inserisci")) {
				cliente = cs.inserisci(c);
			} else {
				cliente = cs.aggiorna(c);
			}
			List<ClienteDto> lista = new ArrayList<ClienteDto>();
			lista.add(cliente);
			m.addAttribute("listaClienti", lista);
			m.addAttribute("mex", "Operazione avvenuta con successo");

		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		m.addAttribute("vengo","admin");
		
		return "listaClienti";
	}

	@GetMapping("/visualizzaScheda")
	public String visualizzaScheda(Model m, @RequestParam("id") long id) {

		ClienteDto c = cs.getClienteById(id);
		List<ClienteDto> lista = new ArrayList<ClienteDto>();
		lista.add(c);
		m.addAttribute("scheda", c.getScheda());
		m.addAttribute("listaClienti", lista);

		return "listaClienti";
	}

	@GetMapping("/eliminaCliente")
	public String eliminaCliente(@RequestParam("id") long id, Model m) {

		try {
			boolean eliminato = cs.elimina(id);
			if (eliminato) {
				m.addAttribute("mex", "Operazione avvenuta con successo");
				m.addAttribute("listaClienti", cs.getListaClienti());
			}
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		m.addAttribute("vengo","admin");
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
		
		return "listaClienti";
	}

}

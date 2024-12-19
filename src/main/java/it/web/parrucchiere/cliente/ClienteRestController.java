package it.web.parrucchiere.cliente;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cliente")
public class ClienteRestController {

	private final IClienteService cs;

	@Autowired
	public ClienteRestController(IClienteService cs) {
		this.cs = cs;
	}

	@GetMapping("/suggerimenti")
	public List<ClienteDto> suggerimenti(@RequestParam("query") String query) {

		List<ClienteDto> suggerimenti = cs.getListaClientiByIniziali(query);
		return suggerimenti;
	}

	@GetMapping("/dettagli")
	public ClienteDto dettagli(@RequestParam("id") long id) {

		ClienteDto c = cs.getClienteById(id);
		return c;
	}
}

package it.web.parrucchiere.operatore;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.web.parrucchiere.cliente.ClienteDto;

@RestController
@RequestMapping("/api/operatore")
public class OperatoreRestController {

	private final IOperatoreService os;

	@Autowired
	public OperatoreRestController(IOperatoreService os) {
		this.os = os;
	}

	@GetMapping("/suggerimenti")
	public List<OperatoreDto> suggerimenti(@RequestParam("query") String query) {

		List<OperatoreDto> suggerimenti = os.getListaOperatoriByIniziali(query);
		return suggerimenti;
	}

	@GetMapping("/dettagli")
	public OperatoreDto dettagli(@RequestParam("id") long id) {

		OperatoreDto o = os.getOperatoreById(id);
		return o;
	}
}

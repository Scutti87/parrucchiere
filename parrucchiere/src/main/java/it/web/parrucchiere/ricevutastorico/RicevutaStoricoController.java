package it.web.parrucchiere.ricevutastorico;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.web.parrucchiere.cliente.ClienteDto;
import it.web.parrucchiere.cliente.IClienteService;
import it.web.parrucchiere.exception.BusinessException;
import it.web.parrucchiere.ricevuta.IRicevutaService;
import it.web.parrucchiere.ricevuta.RicevutaDto;
import it.web.parrucchiere.servizio.ServizioDto;

@Controller
@RequestMapping("ricevutaStorico")
public class RicevutaStoricoController {

	private final IClienteService cs;
	private final IRicevutaStoricoService rs;
	private final IRicevutaService rservice;

	@Autowired
	public RicevutaStoricoController(IRicevutaStoricoService rs, IClienteService cs, IRicevutaService rservice) {
		this.cs = cs;
		this.rservice = rservice;
		this.rs = rs;
	}

	@GetMapping("/getListaRicevuteStorico")
	public String getListaRicevuteStorico(Model m) {

		try {
			List<RicevutaStoricoDto> lista = rs.getListaRicevuteStorico();
			m.addAttribute("listaRicevute", lista);
			m.addAttribute("vengos", "storico");
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		return "listaRicevute";
	}

	@GetMapping("/cercaRicevutaStorico")
	public String cercaRicevutaStorico(@RequestParam("clienteIdRic") long id, Model m) {

		try {
			ClienteDto c = cs.getClienteById(id);
			List<RicevutaStoricoDto> lista = rs.getListaRicevuteStoricoByCliente(c.getId());

			m.addAttribute("listaRicevute", lista);
			m.addAttribute("vengos", "storico");
		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}

		return "listaRicevute";
	}

	
	@GetMapping("/aggiornaStorico")
	public String aggiornaStorico(Model m) {
		try {
			List<RicevutaDto> listaRic = rservice.getListaRicevute().stream()
					.filter(a -> a.getData().toLocalDate().isBefore(LocalDate.now())).toList();

			if (!listaRic.isEmpty()) {

				for (int i = 0; i < listaRic.size(); i++) {

					if (listaRic.get(i).getTotale() != 0 && listaRic.get(i).getOperatore() != null) {
						RicevutaStoricoDto a = trasforma(listaRic.get(i));
						rs.aggiornaStorico(a);						
						
						for (ServizioDto ricevutaDto : a.getServizi()) {
							
							rs.aggiornaStoricoServizi(a.getId(), ricevutaDto.getId());
						}

							rservice.elimina(listaRic.get(i).getId());					
					}

				}
			}

			m.addAttribute("listaRicevute", rservice.getListaRicevute());

		} catch (BusinessException e) {
			m.addAttribute("errorMessage", e.getMessage());
		}
		m.addAttribute("vengo", "admin");

		return "listaRicevute";
	}

	private RicevutaStoricoDto trasforma(RicevutaDto a) {
		RicevutaStoricoDto r = new RicevutaStoricoDto();
		r.setCliente(a.getCliente());
		r.setData(a.getData());
		r.setId(a.getId());
		r.setOperatore(a.getOperatore());
		r.setServizi(a.getServizi());
		r.setTotale(a.getTotale());
		return r;
	}

}

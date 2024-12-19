package it.web.parrucchiere.ricevutastorico;

import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.web.parrucchiere.exception.BusinessException;

@Service
public class RicevutaStoricoServiceImpl implements IRicevutaStoricoService {

	private final IRicevutaStoricoRepository rr;
	private final ModelMapper mm;

	@Autowired
	public RicevutaStoricoServiceImpl(IRicevutaStoricoRepository rr, ModelMapper mm) {
		this.rr = rr;
		this.mm = mm;
	}

	@Override
	public RicevutaStoricoDto getRicevutaStoricoById(long id) {

		RicevutaStorico r = rr.findById(id)
				.orElseThrow(() -> new BusinessException(String.format("Ricevuta con id %d non presente in DB", id)));

		return mm.map(r, RicevutaStoricoDto.class);
	}

	@Override
	public List<RicevutaStoricoDto> getListaRicevuteStorico() {

		List<RicevutaStoricoDto> listaDto = new ArrayList<RicevutaStoricoDto>();
		List<RicevutaStorico> lista = rr.findAllByOrderByDataDesc();

		

		lista.forEach(r -> listaDto.add(mm.map(r, RicevutaStoricoDto.class)));

		return listaDto;
	}

	@Override
	public List<RicevutaStoricoDto> getListaRicevuteStoricoByCliente(long id) {

		List<RicevutaStoricoDto> listaDto = new ArrayList<RicevutaStoricoDto>();
		List<RicevutaStorico> lista = rr.getListaRicevuteByCliente(id);

		if (lista.isEmpty()) {
			throw new BusinessException(String.format("Nessun Ricevuta presente in DB"));
		}

		lista.forEach(r -> listaDto.add(mm.map(r, RicevutaStoricoDto.class)));

		return listaDto;
	}

	@Override
	public void aggiornaStorico(RicevutaStoricoDto r) {

		try {
			r.setId(rr.recuperaUltimoId() + 1);
		} catch (Exception e) {
			r.setId(1);
		}
		
		if (rr.findAll().stream().noneMatch(
				ric -> ric.getCliente().getId() == r.getCliente().getId() && ric.getData().equals(r.getData()))) {
			rr.aggiornaStorico(r.getId(), r.getData(), r.getTotale(), r.getCliente().getId(), r.getOperatore().getId());
		}
	}

	@Override
	public void aggiornaStoricoServizi(long ricid, long servid) {

		rr.aggiornaStoricoServizi(ricid, servid);
	}

}

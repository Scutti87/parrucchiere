package it.web.parrucchiere.ricevuta;

import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.web.parrucchiere.exception.BusinessException;

@Service
public class RicevutaServiceImpl implements IRicevutaService {

	private final IRicevutaRepository rr;
	private final ModelMapper mm;

	@Autowired
	public RicevutaServiceImpl(IRicevutaRepository rr, ModelMapper mm) {
		this.rr = rr;
		this.mm = mm;
	}

	@Override
	public RicevutaDto inserisci(RicevutaDto r) {

		long id = 0;
		List<Ricevuta> lista = rr.findAll();

		if (lista.isEmpty()) {
			id = 1;
		} else {

			if (lista.stream().anyMatch(
					ric -> ric.getData().equals(r.getData()) && ric.getCliente().getId() == r.getCliente().getId())) {
				throw new BusinessException("Ricevuta già presente in DB");
			}
			id = rr.recuperaUltimoId() + 1;
		}

		rr.setAutoIncrement(id);
		rr.save(mm.map(r, Ricevuta.class));

		return mm.map(rr.findById(id), RicevutaDto.class);
	}

	@Override
	public RicevutaDto aggiorna(RicevutaDto r) {

		rr.findById(r.getId()).orElseThrow(
				() -> new BusinessException(String.format("Ricevuta con id %d non presente in DB", r.getId())));

		List<Ricevuta> lista = rr.findAll();
		Ricevuta ricevuta = lista.stream().filter(ric -> ric.getId() == r.getId()).findFirst().orElse(null);
		lista.remove(ricevuta);

		if (lista.stream().anyMatch(
				ric -> ric.getData().equals(r.getData()) && ric.getCliente().getId() == r.getCliente().getId())) {
			throw new BusinessException("Ricevuta già presente in DB");
		}

		rr.save(mm.map(r, Ricevuta.class));

		return mm.map(rr.findById(r.getId()), RicevutaDto.class);
	}

	@Override
	public boolean elimina(long id) {

		rr.findById(id).orElseThrow(
				() -> new BusinessException(String.format("Ricevuta con id %d non presente in DB", id)));

		rr.deleteById(id);

		return rr.findById(id).isEmpty();
	}

	@Override
	public RicevutaDto getRicevutaById(long id) {

		Ricevuta r = rr.findById(id).orElseThrow(
				() -> new BusinessException(String.format("Ricevuta con id %d non presente in DB", id)));

		return mm.map(r, RicevutaDto.class);
	}

	@Override
	public List<RicevutaDto> getListaRicevute() {

		List<RicevutaDto> listaDto = new ArrayList<RicevutaDto>();
		List<Ricevuta> lista = rr.findAllByOrderByDataAsc();

		lista.forEach(r -> listaDto.add(mm.map(r, RicevutaDto.class)));

		return listaDto;
	}

	@Override
	public List<RicevutaDto> getListaRicevuteByCliente(long id) {

		List<RicevutaDto> listaDto = new ArrayList<RicevutaDto>();
		List<Ricevuta> lista = rr.getListaRicevuteByCliente(id);

		if (lista.isEmpty()) {
			throw new BusinessException(String.format("Nessun Ricevuta presente in DB"));
		}

		lista.forEach(r -> listaDto.add(mm.map(r, RicevutaDto.class)));

		return listaDto;
	}

	
}

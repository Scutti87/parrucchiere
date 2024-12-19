package it.web.parrucchiere.servizio;

import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.web.parrucchiere.exception.BusinessException;

@Service
public class ServizioServiceImpl implements IServizioService {

	private final IServizioRepository sr;
	private final ModelMapper mm;

	@Autowired
	public ServizioServiceImpl(IServizioRepository sr, ModelMapper mm) {
		this.sr = sr;
		this.mm = mm;
	}

	@Override
	public ServizioDto getServizioById(long id) {

		Servizio a = sr.findById(id).orElseThrow(
				() -> new BusinessException(String.format("Servizio con id %d non presente in DB", id)));

		return mm.map(a, ServizioDto.class);
	}

	@Override
	public List<ServizioDto> getListaServizi() {
		
		List<ServizioDto> listaDto = new ArrayList<ServizioDto>();
		List<Servizio> lista = sr.findAllByOrderByNomeAscPrezzoAsc();

		if (lista.isEmpty()) {
			throw new BusinessException(String.format("Nessun Servizio presente in DB"));
		}

		lista.forEach(a -> listaDto.add(mm.map(a, ServizioDto.class)));

		return listaDto;
	}

	@Override
	public List<ServizioDto> getListaNomiServizi() {

		List<ServizioDto> listaDto = new ArrayList<ServizioDto>();
		List<Servizio> lista = sr.getListaNomiServizi();

		if (lista.isEmpty()) {
			throw new BusinessException(String.format("Nessun Servizio presente in DB"));
		}		
		lista.forEach(a -> listaDto.add(mm.map(a, ServizioDto.class)));
		
		return listaDto;
	}

	

}

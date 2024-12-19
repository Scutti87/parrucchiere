package it.web.parrucchiere.operatore;

import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.web.parrucchiere.appuntamento.Appuntamento;
import it.web.parrucchiere.appuntamento.AppuntamentoDto;
import it.web.parrucchiere.exception.BusinessException;

@Service
public class OperatoreServiceImpl implements IOperatoreService {

	private final IOperatoreRepository or;
	private final ModelMapper mm;

	@Autowired
	public OperatoreServiceImpl(IOperatoreRepository or, ModelMapper mm) {
		this.or = or;
		this.mm = mm;
	}

	@Override
	public OperatoreDto getOperatoreById(long id) {

		Operatore o = or.findById(id).orElseThrow(
				() -> new BusinessException(String.format("Operatore con id %d non presente in DB", id)));

		return mm.map(o, OperatoreDto.class);
	}

	@Override
	public List<OperatoreDto> getListaOperatori() {

		List<OperatoreDto> listaDto = new ArrayList<OperatoreDto>();
		List<Operatore> lista = or.findAll();

		if (lista.isEmpty()) {
			throw new BusinessException(String.format("Nessun Operatore presente in DB"));
		}

		lista.forEach(o -> listaDto.add(mm.map(o, OperatoreDto.class)));

		return listaDto;
	}

	@Override
	public List<OperatoreDto> getListaOperatoriByIniziali(String iniziali) {

		List<OperatoreDto> listaDto = new ArrayList<OperatoreDto>();
		List<Operatore> lista = or.findByNomeStartingWith(iniziali);

		if (lista.isEmpty()) {
			throw new BusinessException(String.format("Nessun Operatore presente in DB"));
		}

		lista.forEach(o -> listaDto.add(mm.map(o, OperatoreDto.class)));

		return listaDto;
	}

	
	

}

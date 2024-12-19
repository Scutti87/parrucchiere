package it.web.parrucchiere.appuntamentostorico;

import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.web.parrucchiere.exception.BusinessException;

@Service
public class AppuntamentoStoricoServiceImpl implements IAppuntamentoStoricoService {

	private final IAppuntamentoStoricoRepository ar;
	private ModelMapper mm;

	@Autowired
	public AppuntamentoStoricoServiceImpl(IAppuntamentoStoricoRepository ar, ModelMapper mm) {
		this.ar = ar;
		this.mm = mm;
	}

	@Override
	public AppuntamentoStoricoDto getAppuntamentoStoricoById(long id) {

		AppuntamentoStorico a = ar.findById(id).orElseThrow(
				() -> new BusinessException(String.format("Appuntamento con id %d non presente in DB", id)));

		return mm.map(a, AppuntamentoStoricoDto.class);
	}

	@Override
	public List<AppuntamentoStoricoDto> getListaAppuntamentiStorico() {

		List<AppuntamentoStoricoDto> listaDto = new ArrayList<AppuntamentoStoricoDto>();
		List<AppuntamentoStorico> lista = ar.findAllByOrderByDataDescOraAsc();

		if (lista.isEmpty()) {
			throw new BusinessException(String.format("Nessun Appuntamento presente in DB"));
		}

		lista.forEach(a -> listaDto.add(mm.map(a, AppuntamentoStoricoDto.class)));

		return listaDto;
	}

	@Override
	public List<AppuntamentoStoricoDto> getListaAppuntamentiStoricoByCliente(long id) {

		List<AppuntamentoStoricoDto> listaDto = new ArrayList<AppuntamentoStoricoDto>();
		List<AppuntamentoStorico> lista = ar.getListaAppuntamentiStoricoByCliente(id);

		if (lista.isEmpty()) {
			throw new BusinessException(String.format("Nessun Appuntamento presente in DB"));
		}

		lista.forEach(a -> listaDto.add(mm.map(a, AppuntamentoStoricoDto.class)));

		return listaDto;
	}

	@Transactional
	public void aggiornaStorico(AppuntamentoStoricoDto appuntamentoStoricoDto) {

		appuntamentoStoricoDto.setId(ar.recuperaUltimoId() + 1);
		ar.aggiornaStorico(appuntamentoStoricoDto.getId(), appuntamentoStoricoDto.getData(),
				appuntamentoStoricoDto.getOra(), appuntamentoStoricoDto.getCliente().getId(),
				appuntamentoStoricoDto.getOperatore().getId());
	}

	@Transactional
	public void aggiornaStoricoServizi(long appid, long servid) {
		ar.aggiornaStoricoServizi(appid, servid);
	}
}

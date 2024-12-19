package it.web.parrucchiere.appuntamento;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.web.parrucchiere.cliente.Cliente;
import it.web.parrucchiere.exception.BusinessException;
import it.web.parrucchiere.operatore.Operatore;
import it.web.parrucchiere.servizio.Servizio;

@Service
public class AppuntamentoServiceImpl implements IAppuntamentoService {

	private final IAppuntamentoRepository ar;
	private ModelMapper mm;

	@Autowired
	public AppuntamentoServiceImpl(IAppuntamentoRepository ar, ModelMapper mm) {
		this.ar = ar;
		this.mm = mm;
	}

	@Override
	public AppuntamentoDto inserisci(AppuntamentoDto a) {

		long id = 0;
		List<Appuntamento> lista = ar.findAll();

		if (lista.isEmpty()) {
			id = 1;
		} else {

			if (lista.stream().anyMatch(
					app -> app.getData().equals(a.getData()) && app.getCliente().getId() == a.getCliente().getId())) {
				throw new BusinessException("Appuntamento già presente in DB");
			}
			id = ar.recuperaUltimoId() + 1;
		}

		ar.setAutoIncrement(id);
		ar.save(mm.map(a, Appuntamento.class));

		return mm.map(ar.findById(id), AppuntamentoDto.class);
	}

	@Override
	public AppuntamentoDto aggiorna(AppuntamentoDto a) {

		ar.findById(a.getId()).orElseThrow(
				() -> new BusinessException(String.format("Appuntamento con id %d non presente in DB", a.getId())));

		List<Appuntamento> lista = ar.findAll();
		Appuntamento appuntamento = lista.stream().filter(app -> app.getId() == a.getId()).findFirst().orElse(null);
		lista.remove(appuntamento);

		if (lista.stream().anyMatch(
				app -> app.getData().equals(a.getData()) && app.getCliente().getId() == a.getCliente().getId())) {
			throw new BusinessException("Appuntamento già presente in DB");
		}

		ar.save(mm.map(a, Appuntamento.class));

		return mm.map(ar.findById(a.getId()), AppuntamentoDto.class);
	}

	@Override
	public boolean elimina(long id) {

		ar.findById(id).orElseThrow(
				() -> new BusinessException(String.format("Appuntamento con id %d non presente in DB", id)));

		ar.deleteById(id);

		return ar.findById(id).isEmpty();

	}

	@Override
	public AppuntamentoDto getAppuntamentoById(long id) {

		Appuntamento a = ar.findById(id).orElseThrow(
				() -> new BusinessException(String.format("Appuntamento con id %d non presente in DB", id)));

		return mm.map(a, AppuntamentoDto.class);
	}

	@Override
	public List<AppuntamentoDto> getListaAppuntamenti() {

		List<AppuntamentoDto> listaDto = new ArrayList<AppuntamentoDto>();
		List<Appuntamento> lista = ar.findAllByOrderByDataAscOraAsc();

		if (lista.isEmpty()) {
			Appuntamento a = new Appuntamento();
			Cliente c = new Cliente();
			Operatore o = new Operatore();
			Servizio s = new Servizio();
			s.setNome(" ");
			c.setNome("Pinco");
			c.setCognome("Pallo");
			o.setNome("Pinco");
			o.setCognome("Pallo");
			a.setCliente(c);
			a.setOperatore(o);
			a.setServizi(List.of(s));
			a.setData(Date.valueOf(LocalDate.now()));
			a.setId(1);
			a.setOra(Time.valueOf(LocalTime.now()));		
			lista.add(a);
		}

		lista.forEach(a -> listaDto.add(mm.map(a, AppuntamentoDto.class)));

		return listaDto;
	}

	@Override
	public List<AppuntamentoDto> getListaAppuntamentiByCliente(long id) {

		List<AppuntamentoDto> listaDto = new ArrayList<AppuntamentoDto>();
		List<Appuntamento> lista = ar.getListaAppuntamentiByCliente(id);

		if (lista.isEmpty()) {
			throw new BusinessException(String.format("Nessun Appuntamento presente in DB"));
		}

		lista.forEach(a -> listaDto.add(mm.map(a, AppuntamentoDto.class)));

		return listaDto;
	}

}

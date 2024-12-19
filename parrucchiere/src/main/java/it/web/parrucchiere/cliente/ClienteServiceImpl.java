package it.web.parrucchiere.cliente;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.web.parrucchiere.exception.BusinessException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ClienteServiceImpl implements IClienteService {

	private final IClienteRepository cr;
	private final ModelMapper mm;

	@Autowired
	public ClienteServiceImpl(IClienteRepository cr, ModelMapper mm) {
		this.cr = cr;
		this.mm = mm;
	}

	@Override
	public ClienteDto inserisci(ClienteDto c) {

		c.trimCampi();
		c.setDataRegistrazione(Date.valueOf(LocalDate.now()));
		long id = 0;
		List<Cliente> lista = cr.findAll();

		if (lista.isEmpty()) {
			id = 1;
		} else {

			if (lista.stream().anyMatch(cli -> cli.getNome().equalsIgnoreCase(c.getNome())
					&& cli.getCognome().equalsIgnoreCase(c.getCognome()) && cli.getDdn().equals(c.getDdn()))) {
				throw new BusinessException("Cliente già presente in DB");
			}

			if (lista.stream().anyMatch(cli -> cli.getEmail().equals(c.getEmail()) && !cli.getEmail().equals(""))) {
				throw new BusinessException("Email già presente in DB");
			}

			if (lista.stream().anyMatch(cli -> cli.getTelefono().equals(c.getTelefono()))) {
				throw new BusinessException("Telefono già presente in DB");
			}

			id = cr.recuperaUltimoId() + 1;
		}
		cr.setAutoIncrement(id);
		cr.save(mm.map(c, Cliente.class));

		return mm.map(cr.findById(id), ClienteDto.class);
	}

	@Override
	public ClienteDto aggiorna(ClienteDto c) {

		c.trimCampi();
		c.setDataRegistrazione(
				c.getDataRegistrazione() == null ? Date.valueOf(LocalDate.now()) : c.getDataRegistrazione());

		cr.findById(c.getId()).orElseThrow(
				() -> new BusinessException(String.format("Cliente con id %d non presente in DB", c.getId())));

		List<Cliente> lista = cr.findAll();
		Cliente cliente = lista.stream().filter(cli -> cli.getId() == c.getId()).findFirst().orElse(null);
		lista.remove(cliente);

		if (lista.stream().anyMatch(cli -> cli.getNome().equalsIgnoreCase(c.getNome().trim())
				&& cli.getCognome().equalsIgnoreCase(c.getCognome().trim()) && cli.getDdn().equals(c.getDdn()))) {
			throw new BusinessException("Cliente già presente in DB");
		}

		if (lista.stream().anyMatch(cli -> cli.getEmail().equals(c.getEmail()) && !cli.getEmail().equals(""))) {
			throw new BusinessException("Email già presente in DB");
		}

		if (lista.stream().anyMatch(cli -> cli.getTelefono().equals(c.getTelefono()))) {
			throw new BusinessException("Telefono già presente in DB");
		}

		cr.save(mm.map(c, Cliente.class));

		return mm.map(cr.findById(c.getId()), ClienteDto.class);
	}

	@Override
	public boolean elimina(long id) {

		cr.findById(id)
				.orElseThrow(() -> new BusinessException(String.format("Cliente con id %d non presente in DB", id)));

		cr.deleteById(id);

		return cr.findById(id).isEmpty();
	}

	@Override
	public ClienteDto getClienteById(long id) {

		Cliente c = cr.findById(id)
				.orElseThrow(() -> new BusinessException(String.format("Cliente con id %d non presente in DB", id)));

		return mm.map(c, ClienteDto.class);
	}

	@Override
	public List<ClienteDto> getListaClienti() {

		List<ClienteDto> listaDto = new ArrayList<ClienteDto>();
		List<Cliente> lista = cr.findAll();

		if (lista.isEmpty()) {
			throw new BusinessException(String.format("Nessun cliente presente in DB"));
		}

		lista.forEach(c -> listaDto.add(mm.map(c, ClienteDto.class)));

		return listaDto;
	}

	@Override
	public List<ClienteDto> getListaClientiByIniziali(String iniziali) {
		
		List<ClienteDto> listaDto = new ArrayList<ClienteDto>();
		List<Cliente> lista = cr.findByNomeStartingWith(iniziali);
		
		lista.forEach(c -> listaDto.add(mm.map(c, ClienteDto.class)));
		
		return listaDto;
	}

}

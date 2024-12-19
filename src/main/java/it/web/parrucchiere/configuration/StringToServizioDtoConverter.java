package it.web.parrucchiere.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import it.web.parrucchiere.servizio.IServizioService;
import it.web.parrucchiere.servizio.ServizioDto;

@Component
public class StringToServizioDtoConverter implements Converter<String, ServizioDto> {

	 @Autowired
	    private IServizioService servizioService;

	    @Override
	    public ServizioDto convert(String source) {
	    	System.out.println("Converter chiamato con valore: " + source);
	        Long id = Long.valueOf(source);
	        return servizioService.getServizioById(id);
	    }
}

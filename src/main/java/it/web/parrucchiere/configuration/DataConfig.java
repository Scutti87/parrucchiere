package it.web.parrucchiere.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import it.web.parrucchiere.appuntamento.Appuntamento; 
import it.web.parrucchiere.appuntamento.AppuntamentoDto;

@Configuration
public class DataConfig {

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        

        return modelMapper;
    }
}

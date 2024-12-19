package it.web.parrucchiere.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final StringToServizioDtoConverter stringToServizioDtoConverter;


	public WebConfig(StringToServizioDtoConverter stringToServizioDtoConverter
			) {
		this.stringToServizioDtoConverter = stringToServizioDtoConverter;
		
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(stringToServizioDtoConverter);
	
		System.out.println("Converters registrati.");
	}
}

package it.web.parrucchiere.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Time;

@Component
public class SqlTimeToStringConverter implements Converter<Time, String> {
    @Override
    public String convert(Time source) {
        return source.toString().substring(0, 5); // Restituisce solo HH:mm
    }
}
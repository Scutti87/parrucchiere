package it.web.parrucchiere.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalTime;

@Component
public class StringToSqlTimeConverter implements Converter<String, Time> {
    @Override
    public Time convert(String source) {
        try {
            return Time.valueOf(LocalTime.parse(source).toString());
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato orario non valido: " + source);
        }
    }
}

package org.springframework.samples.petclinic.vacination;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class VaccineFormatter implements Formatter<Vaccine>{

    @Override
    public String print(Vaccine object, Locale locale) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Vaccine parse(String text, Locale locale) throws ParseException {
        // TODO Auto-generated method stub
        return null;
    }
    
}

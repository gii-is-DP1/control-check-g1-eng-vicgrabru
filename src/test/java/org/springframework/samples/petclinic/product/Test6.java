package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.vacination.VaccinationService;
import org.springframework.samples.petclinic.vacination.Vaccine;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test6 {
    @Autowired
    VaccinationService vs;

    @Test
    public void test6(){
        validateFindVaccineByName();
        validateNotFoundVaccineByName();
    }



    public void validateFindVaccineByName(){
        String typeName="Anti-rabid";
        Vaccine vaccine=vs.getVaccine(typeName);
        assertNotNull(vaccine);
        assertEquals(typeName,vaccine.getName());
    }

    
    public void validateNotFoundVaccineByName(){
        String typeName="This is not a valid vaccine name";
        Vaccine vaccine=vs.getVaccine(typeName);
        assertNull(vaccine);
    }

}

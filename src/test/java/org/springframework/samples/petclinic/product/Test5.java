package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.vacination.Vaccination;
import org.springframework.samples.petclinic.vacination.VaccinationService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test5 {

    @Autowired(required = false)
    VaccinationService ps;

    @Test public void test5(){

        vaccinationServiceIsInjected();
        vaccinationServiceCanGetVaccinations();
    }


    

    
    public void vaccinationServiceIsInjected()
    {
        assertNotNull(ps,"VaccinationService was not injected by spring");       
    }

    
    public void vaccinationServiceCanGetVaccinations(){
        assertNotNull(ps,"VaccinationService was not injected by spring");
        List<Vaccination> vaccinations=ps.getAll();
        assertNotNull(vaccinations,"The list of vaccinations found by the VaccinationService was null");
        assertFalse(vaccinations.isEmpty(),"The list of vaccinations found by the VaccinationService was empty");       
    }
}

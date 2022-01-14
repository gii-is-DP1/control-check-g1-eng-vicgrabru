package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.util.Optional;

import javax.persistence.ManyToOne;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.vacination.Vaccination;
import org.springframework.samples.petclinic.vacination.VaccinationRepository;
import org.springframework.samples.petclinic.vacination.Vaccine;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test4 {
    @Autowired
    VaccinationRepository vr;
    

    @Test
    @DisplayName("The DB is initialized with two vaccinations associated to two vaccines")
    public void testInitialVaccinationsLinked(){
        testAnnotations();
        testInitialvaccinationsLinked();
    }

    void testAnnotations(){
        try{
            Field vaccine=Vaccination.class.getDeclaredField("vaccine");
            ManyToOne annotationManytoOne=vaccine.getAnnotation(ManyToOne.class);
            assertNotNull(annotationManytoOne,"The vaccine property is not properly annotated");
        }catch(NoSuchFieldException ex){
            fail("The vaccination class should have an attribute called vaccine that is not present: "+ex.getMessage());
        }
    }    

    void testInitialvaccinationsLinked() {
        Field vaccineField;
        Vaccine vaccine;

        try {
            vaccineField = Vaccination.class.getDeclaredField("vaccine");
            vaccineField.setAccessible(true);
            Optional<Vaccination> v1=vr.findById(1);
            assertTrue(v1.isPresent(),"vaccination with id:1 not found");
            vaccine = (Vaccine) vaccineField.get(v1.get());
            assertNotNull(vaccine,"The vaccination with id:1 has not a vaccine associated");
            assertEquals(2,vaccine.getId(),"The id of the vaccine associated to the vaccination with id:1 is not 1");

            Optional<Vaccination> v2=vr.findById(2);
            assertTrue(v2.isPresent(),"vaccination with id:2 not found");
            vaccine = (Vaccine) vaccineField.get(v2.get());
            assertNotNull(vaccine,"The vaccination with id:2 has not a vaccine associated");
            assertEquals(1, vaccine.getId(),"The id of the vaccine associated to the vaccination with id:2 is not 2");
        } catch (NoSuchFieldException e) {
            fail("The vaccination class should have an attribute called vaccine that is not present: "+e.getMessage());
        } catch (IllegalArgumentException e) {
            fail("The vaccination class should have an attribute called vaccine that is not present: "+e.getMessage());
        } catch (IllegalAccessException e) {
            fail("The vaccination class should have an attribute called vaccine that is not present: "+e.getMessage());
        }
    } 
     
}

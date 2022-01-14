package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.PetRepository;
import org.springframework.samples.petclinic.vacination.Vaccination;
import org.springframework.samples.petclinic.vacination.VaccinationRepository;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test1 {

    @Autowired(required = false)
    VaccinationRepository vr;

    @Autowired
    PetRepository pr;
    

    @Test
    public void test1(){
        repositoryExists();
        repositoryContainsMethod();
        testConstraints();
        testAnnotations();
    }

    
    public void repositoryExists(){
        assertNotNull(vr,"The repository was not injected into the tests, its autowired value was null");
    }

    
    public void repositoryContainsMethod(){
        if(vr!=null){
            Optional<Vaccination> v=vr.findById(12);
            assertFalse(v.isPresent(),"No result (null) should be returned for a vaccine that does not exist");
        }else
            fail("The repository was not injected into the tests, its autowired value was null");
    }

     void testConstraints(){
        Vaccination v=new Vaccination();
        Pet pet=pr.findById(1);
        v.setId(7);
        v.setVaccinatedPet(pet);
        assertThrows(ConstraintViolationException.class,() -> vr.save(v),
        "You are not constraining "+
        "the date property, it should be mandatory");
        v.setDate(LocalDate.of(2021, 12, 31));
        v.setVaccinatedPet(null);
        assertThrows(Exception.class,() -> vr.save(v),
        "You are not constraining the vaccinated pet to be mandatory");
    }

    void testAnnotations(){
        try{
            Field vaccinatedPet=Vaccination.class.getDeclaredField("vaccinatedPet");
            ManyToOne annotationManytoOne=vaccinatedPet.getAnnotation(ManyToOne.class);
            assertNotNull(annotationManytoOne,"The vaccinatedPet property is not properly annotated");
            JoinColumn annotationJoinColumn=vaccinatedPet.getAnnotation(JoinColumn.class);
            assertNotNull(annotationJoinColumn,"The vaccinatedPet property is not properly annotated");
            assertEquals("vaccinated_pet_id",annotationJoinColumn.name(),"The name of the join column in vaccinatedpet is not properly customized");
            Field date=Vaccination.class.getDeclaredField("date");
            DateTimeFormat dateformat=date.getAnnotation(DateTimeFormat.class);
            assertNotNull(dateformat, "The date property is not annotated with a DateTimeFormat");
            assertEquals(dateformat.pattern(),"yyyy/MM/dd");
        }catch(NoSuchFieldException ex){
            fail("The Vaccination class should have a field that is not present: "+ex.getMessage());
        }
    }
}

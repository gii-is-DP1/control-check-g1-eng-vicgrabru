package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.pet.PetRepository;
import org.springframework.samples.petclinic.vacination.VaccinationRepository;
import org.springframework.samples.petclinic.vacination.Vaccine;

@DataJpaTest
public class Test2 {
    @Autowired(required = false)
    VaccinationRepository vr;
    @Autowired
    PetRepository pr;
    @Autowired(required = false)
    EntityManager em;
    
    @Test
    public void test2(){
        entityExists();
        repositoryContainsMethod();
        testConstraints();
        testAnnotations();
    }

   
    public void entityExists()
    {
        Vaccine p=new Vaccine();
        p.setId(7);
        p.setName("Vaccine against The Blight");        
    }  

    
    public void repositoryContainsMethod(){
        try {
            Method findAllVaccines = VaccinationRepository.class.getDeclaredMethod("findAllVaccines");
            if(vr!=null){
                List<Vaccine> pts= (List<Vaccine>) findAllVaccines.invoke(vr);
                assertNotNull(pts,"We can not query all the feeding types through the repository");
            }else
                fail("The repository was not injected into the tests, its autowired value was null");
        } catch(NoSuchMethodException e) {
            fail("There is no method findAllVaccines in FeedingRepository", e);
        } catch (IllegalAccessException e) {
            fail("There is no public method findAllVaccines in FeedingRepository", e);
        } catch (IllegalArgumentException e) {
            fail("There is no method findAllVaccines() in VaccinationRepository", e);
        } catch (InvocationTargetException e) {
            fail("There is no method findAllVaccines() in VaccinationRepository", e);
        }
    }   

    
    public void testConstraints(){
        
        Vaccine vaccine=new Vaccine();       
        vaccine.setName("ja");
        vaccine.setPrice(1.0);
        vaccine.setPetType(pr.findPetTypes().get(0));
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        assertFalse(validator.validate(vaccine).isEmpty());
        vaccine.setName("En un lugar de la mancha, de cuyo nombre no quiero acordarme, no ha mucho tiempo que vivía un hidalgo de los de lanza en astillero, adarga antigua, rocín flaco y galgo corredor.");
        assertFalse(validator.validate(vaccine).isEmpty());
        vaccine.setName("prueba");
        vaccine.setPrice(-1.0);
        assertFalse(validator.validate(vaccine).isEmpty());
        vaccine.setPrice(5.0);
        em.persist(vaccine);
        
        Vaccine pt2=new Vaccine();       
        pt2.setName("prueba");
        pt2.setPrice(2.0);
        assertThrows(Exception.class,()->em.persist(pt2),"Debería lanzarse una excepción al intentar grabar una vacuna sin tipo de mascota asociada");
        
    }

    void testAnnotations(){
        try{
            Field vaccinatedPet=Vaccine.class.getDeclaredField("petType");
            ManyToOne annotationManytoOne=vaccinatedPet.getAnnotation(ManyToOne.class);
            assertNotNull(annotationManytoOne,"The petType property is not properly annotated");
            JoinColumn annotationJoinColumn=vaccinatedPet.getAnnotation(JoinColumn.class);
            assertNotNull(annotationJoinColumn,"The petType property is not properly annotated");
            assertEquals("pet_type_id",annotationJoinColumn.name(),"The name of the join column in petType is not properly customized");
        }catch(NoSuchFieldException ex){
            fail("The Vaccine class should have a field that is not present: "+ex.getMessage());
        }
    }
}

package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.PetService;
import org.springframework.samples.petclinic.vacination.UnfeasibleVaccinationException;
import org.springframework.samples.petclinic.vacination.Vaccination;
import org.springframework.samples.petclinic.vacination.VaccinationService;
import org.springframework.samples.petclinic.vacination.Vaccine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.bytebuddy.asm.Advice.Local;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test8 {
    @Autowired
    VaccinationService vs;
    @Autowired
    PetService ps;


    @Test
    public void test8(){
        testSaveVaccinationSuccessfull();
        testSaveUnfeasibleVaccination();
        testAnnotations();
    }

    @Transactional
    public void testSaveVaccinationSuccessfull()
    {
        Pet pet=ps.findPetById(1); // This pet is Leo, a cat
        Vaccine vaccine=vs.getVaccine("Covid19 for Cats"); // This vaccine is intended for cats
        Vaccination v=new Vaccination();
        v.setDate(LocalDate.now());
        v.setVaccinatedPet(pet);
        setVaccine(v,vaccine);
        try {
            vs.save(v);
        } catch (UnfeasibleVaccinationException e) {
            fail("The excepcion should not be thrown, the vaccination is feasible!");
        }
    }

    @Transactional
    public void testSaveUnfeasibleVaccination()
    {
        Pet pet=ps.findPetById(1); // This pet is Leo, a cat
        Vaccine vaccine=vs.getVaccine("Anti-rabid"); // The anti-rabid vaccine is intended for dogs
        Vaccination v=new Vaccination();
        v.setDate(LocalDate.now());
        v.setVaccinatedPet(pet);
        setVaccine(v,vaccine);
        // Thus, the save should throw an exception:
        assertThrows(UnfeasibleVaccinationException.class,
            ()-> vs.save(v));
    }

    private void setVaccine(Vaccination v, Vaccine vaccine) {
        try {
            Field vaccineField = Vaccination.class.getDeclaredField("vaccine");
            vaccineField.setAccessible(true);
            vaccineField.set(v, vaccine);
        } catch (NoSuchFieldException e) {
            fail("The Vaccination class should have an attribute called vaccine that is not present: ", e);
        } catch (IllegalArgumentException e) {
            fail("The Vaccination class should have an attribute called vaccine that is not present: ", e);
        } catch (IllegalAccessException e) {
            fail("The Vaccination class should have an attribute called vaccine that is not present: ", e);
        }
    }

    public void testAnnotations() {
        Method save=null;
        try {
            save = VaccinationService.class.getDeclaredMethod("save", Vaccination.class);
        } catch (NoSuchMethodException e) {
           fail("VaccinationService no tiene save");
        } catch (SecurityException e) {
            fail("save no es accesible en VaccinationService");
        }
        Transactional transactionalAnnotation=save.getAnnotation(Transactional.class);
        assertNotNull(transactionalAnnotation,"The method save is not annotated as transactional");
        List<Class<? extends Throwable>> exceptionsWithRollbackFor=Arrays.asList(transactionalAnnotation.rollbackFor());
        assertTrue(exceptionsWithRollbackFor.contains(UnfeasibleVaccinationException.class));
    }

}

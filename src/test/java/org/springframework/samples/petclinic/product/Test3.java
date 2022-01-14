package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
public class Test3 {
    @Autowired
    VaccinationRepository vr;

    @Test
    public void test3() {
        testInitialVaccinations();
        testInitialVaccines();
    }

    public void testInitialVaccinations() {
        List<Vaccination> products = vr.findAll();
        assertTrue(products.size() == 2, "Exactly two vaccinations should be present in the DB");

        Optional<Vaccination> p1 = vr.findById(1);
        assertTrue(p1.isPresent(), "There should exist a vaccination with id:1");
        assertEquals(p1.get().getDate(), LocalDate.of(2021, 12, 8),
                "The date of the vaccination with id:1 should be 08/12/2021");
        assertEquals(p1.get().getVaccinatedPet().getName(), "Leo",
                "The name of the vaccinated pet of the vaccination with id:1 should be Leo");

        Optional<Vaccination> p2 = vr.findById(2);
        assertTrue(p2.isPresent(), "There should exist a product with id:2");
        assertEquals(p2.get().getDate(), LocalDate.of(2021, 10, 8),
                "The date of the vaccination with id:2 should be 08/10/2021");
        assertEquals(p2.get().getVaccinatedPet().getName(), "Rosy",
                "The name of the vaccinated pet in the vaccination with id:2 should be Rosy");

    }

    public void testInitialVaccines() {

        try {
            Method findAllVaccines = VaccinationRepository.class.getDeclaredMethod("findAllVaccines");
            if (vr != null) {
                List<Vaccine> vaccines = (List<Vaccine>) findAllVaccines.invoke(vr);
                assertNotNull(vaccines, "We can not query all the feeding types through the repository");
                assertTrue(vaccines.size() == 2, "Exactly two vaccines should be present in the DB");

                for (Vaccine v : vaccines) {
                    if (v.getId() == 1) {
                        assertEquals(v.getName(), "Anti-rabid",
                                "The name of the vaccine with id:1 should be 'Anti-rabid'");
                    } else if (v.getId() == 2) {
                        assertEquals(v.getName(), "Covid19 for Cats",
                                "The name of the vaccine with id:2 should be 'Covid19 for Cats'");
                    } else
                        fail("The id of the product types should be either 1 or 2");
                }
            } else
                fail("The repository was not injected into the tests, its autowired value was null");
        } catch (NoSuchMethodException e) {
            fail("There is no method findAllVaccines in FeedingRepository", e);
        } catch (IllegalAccessException e) {
            fail("There is no public method findAllVaccines in FeedingRepository", e);
        } catch (IllegalArgumentException e) {
            fail("There is no method findAllVaccines() in VaccinationRepository", e);
        } catch (InvocationTargetException e) {
            fail("There is no method findAllVaccines() in VaccinationRepository", e);
        }

    }
}

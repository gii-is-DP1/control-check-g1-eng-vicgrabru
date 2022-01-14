package org.springframework.samples.petclinic.vacination;

import java.util.List;
import java.util.Optional;

public interface VaccinationRepository {
    List<Vaccination> findAll();
    //List<Vaccine> findAllVaccines();
    Optional<Vaccination> findById(int id);
    Vaccination save(Vaccination p);
}

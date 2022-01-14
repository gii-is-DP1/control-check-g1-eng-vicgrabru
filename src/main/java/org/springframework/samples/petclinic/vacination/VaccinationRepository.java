package org.springframework.samples.petclinic.vacination;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationRepository extends CrudRepository<Vaccination, Integer>{
    List<Vaccination> findAll();
    
    @Query("SELECT v FROM Vaccine v")
    List<Vaccine> findAllVaccines();
    
    Optional<Vaccination> findById(int id);
    
    Vaccination save(Vaccination p);
}

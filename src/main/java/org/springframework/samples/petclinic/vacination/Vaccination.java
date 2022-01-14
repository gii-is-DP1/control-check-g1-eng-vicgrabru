package org.springframework.samples.petclinic.vacination;

import java.time.LocalDate;

import javax.persistence.Transient;

import org.springframework.samples.petclinic.pet.Pet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vaccination {
    Integer id;
    LocalDate date;
    Pet vaccinatedPet;   
    @Transient
    Vaccine vaccine; 
}

package org.springframework.samples.petclinic.vacination;

import org.springframework.samples.petclinic.pet.PetType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vaccine {
    Integer id;
    String name;
    Double price;
    PetType petType;
}

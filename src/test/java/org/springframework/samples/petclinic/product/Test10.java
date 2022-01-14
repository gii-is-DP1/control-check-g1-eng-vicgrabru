package org.springframework.samples.petclinic.product;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.pet.PetService;
import org.springframework.samples.petclinic.vacination.UnfeasibleVaccinationException;
import org.springframework.samples.petclinic.vacination.Vaccination;
import org.springframework.samples.petclinic.vacination.VaccinationController;
import org.springframework.samples.petclinic.vacination.VaccinationRepository;
import org.springframework.samples.petclinic.vacination.VaccinationService;
import org.springframework.samples.petclinic.vacination.Vaccine;
import org.springframework.samples.petclinic.vacination.VaccineFormatter;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = VaccinationController.class,
		includeFilters = @ComponentScan.Filter(value = VaccineFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
public class Test10 {
    @MockBean
    VaccinationService vaccinationService;
    @MockBean
	VaccinationRepository vaccinationRepository;
    @MockBean
	PetService petService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void configureMock() throws UnfeasibleVaccinationException{
        String vaccineName="Anti-rabid";
        Vaccine vaccine=new Vaccine();
        vaccine.setName(vaccineName);
        Mockito.when(vaccinationService.save(any(Vaccination.class))).thenReturn(null);
        Mockito.when(vaccinationService.getVaccine(vaccineName)).thenReturn(vaccine);
        List<Vaccine> vaccines=new ArrayList<Vaccine>();
        vaccines.add(vaccine);
        Mockito.when(vaccinationService.getAllVaccines()).thenReturn(vaccines);
    }

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
    void test10()  throws Exception {
        testVaccinationCreationControllerOK();                
        testVaccinationCreationControllerUnfeasibleVaccine();
    }

	void testVaccinationCreationControllerOK() throws Exception {
        mockMvc.perform(post("/vaccination/create")
                            .with(csrf())
                            .param("vaccinatedPet", "3")
                            .param("vaccine", "Anti-rabid")
                            .param("date", "2021/12/31"))
                .andExpect(status().isOk())
				.andExpect(view().name("welcome"));
    }	
    
    void testVaccinationCreationControllerUnfeasibleVaccine() throws Exception {
        Mockito.when(vaccinationService.save(any(Vaccination.class))).thenThrow(new UnfeasibleVaccinationException());
        mockMvc.perform(post("/vaccination/create")
                            .with(csrf())
                            .param("vaccinatedPet", "1")
                            .param("vaccine", "Anti-rabid")
                            .param("date", "2021/12/31"))
                .andExpect(status().isOk())				
				.andExpect(view().name("vaccination/createOrUpdateVaccinationForm"));
    }
}

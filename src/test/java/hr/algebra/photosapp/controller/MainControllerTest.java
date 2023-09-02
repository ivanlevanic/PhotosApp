package hr.algebra.photosapp.controller;

import hr.algebra.photosapp.domain.User;
import hr.algebra.photosapp.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ConsumptionRepository consumptionRepository;

    @Test
    void displayHomepage() throws Exception {
        this.mockMvc
                .perform(
                        get("/homepage")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .with(csrf())
                                .with(user("ivan").password("bok").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("pages/homepage"));
    }

    @Test
    void displayActivityForbidden() throws Exception {
        this.mockMvc
                .perform(
                        get("/activity/statistics")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .with(csrf())
                                .with(user("ivan").password("bok").roles("USER"))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void displayActivityAdmin() throws Exception {
        this.mockMvc
                .perform(
                        get("/activity/statistics")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .with(csrf())
                                .with(user("admin").password("bok").roles("ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("pages/activity"));
    }

    @Test
    public void testRegisterUser_SuccessfulRegistration() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("plan", "PRO");
        params.add("user.firstName", "Testni");
        params.add("user.lastName", "Korisnik");
        params.add("user.email", "tesni@mail.com");
        params.add("user.username", "testUser");
        params.add("user.password", "testnipass");

        mockMvc.perform(post("/registration")
                .params(params)
                .with(user("user").roles("ANONYMOUS"))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));

        User registriraniUser = userRepository.findByUsername("testUser");
        assertNotNull(registriraniUser); //??????
    }

//    @Test
//    public void testRegisterUser_DuplicateUsername() throws Exception {
//        User postojeciKorisnik = new User("testUser", "passw", "testni@email.com", "Postojeci", "Korisnik",  true);
//        userRepository.save(postojeciKorisnik);
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("plan", "PRO");
//        params.add("user.username", "ivan");
//        params.add("user.password", "passw");
//        params.add("user.firstName", "Ime");
//        params.add("user.lastName", "Prezime");
//        params.add("user.email", "neki@email.com");
//
//        mockMvc.perform(post("/registration")
//                .params(params)
//                .with(user("user").roles("ANONYMOUS"))
//                .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("registration"))
//                .andExpect(model().attributeHasFieldErrors("user", "username"))
//                .andExpect(model().attribute("errorMessages", "Username already exists. Choose a unique username."));
//    }


}

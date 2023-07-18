package hr.algebra.photosapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    void displayOneShow() throws Exception {
        this.mockMvc
                .perform(
                        get("/home/show")
                                .param("name", "got")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .with(csrf())
                                .with(user("test").password("password").roles("USER", "ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("pages/services"));
    }

    @Test
    void displayOneShow_noData() throws Exception {
        this.mockMvc
                .perform(
                        get("/home/show")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .with(csrf())
                                .with(user("test").password("password").roles("USER", "ADMIN"))
                )
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/home?error"));
    }

    @Test
    void searchForStreamingServices() throws Exception {
        this.mockMvc
                .perform(
                        post("/home/search")
                                .param("streamingservice", "netflix")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .with(csrf())
                                .with(user("test").password("password").roles("USER", "ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("pages/search"));
    }

}

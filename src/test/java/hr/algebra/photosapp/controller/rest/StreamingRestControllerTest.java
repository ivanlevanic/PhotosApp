package hr.algebra.photosapp.controller.rest;

import hr.algebra.photosapp.domain.delete.TvShowData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import hr.algebra.photosapp.repository.delete.JpaTvShowRepository;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StreamingRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JpaTvShowRepository jpaTvShowRepository;

    @Test
    void findAllShows() throws Exception {
        this.mockMvc.perform(
                get("/api/show")
                        .with(user("test").password("password").roles("ADMIN", "USER"))
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(12)));
    }

    @Test
    void findOneShow() throws Exception {
        final long TEST_ID = 1;
        this.mockMvc.perform(
                        get("/api/show/" + TEST_ID)
                                .with(user("test").password("password").roles("ADMIN", "USER"))
                                .with(csrf())
                )
                .andExpect(status().isOk());
    }

    @Test
    void findOneShow_InvalidID() throws Exception {
        final long TEST_ID = 20;
        this.mockMvc.perform(
                        get("/api/show/" + TEST_ID)
                                .with(user("test").password("password").roles("ADMIN", "USER"))
                                .with(csrf())
                )
                .andExpect(status().isOk());
    }

    @Test
    void save() throws Exception {
        String TEST_NAME = "Fargo";
        float TEST_PRICE = 10;
        String TEST_STREAMINGSERVICE = "HboMAX";
        Integer TEST_SEASONS = 4;

        TvShowData product = new TvShowData();
        product.setName(TEST_NAME);
        product.setPrice(TEST_PRICE);
        product.setStreamingservice(TEST_STREAMINGSERVICE);
        product.setSeasons(TEST_SEASONS);

        this.mockMvc.perform(
                        post("/api/show")
                                .with(user("test").password("password").roles("ADMIN", "USER"))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(TEST_NAME))
                .andExpect(jsonPath("$.price").value(TEST_PRICE))
                .andExpect(jsonPath("$.streamingservice").value(TEST_STREAMINGSERVICE))
                .andExpect(jsonPath("$.seasons").value(TEST_SEASONS));
    }

    @Test
    void delete_noRights() throws Exception {
        final long TEST_DELETE_ID = 1;

        this.mockMvc.perform(
                        delete("/api/show/" + TEST_DELETE_ID)
                                .with(user("test").password("password").roles("USER"))
                                .with(csrf())
                )
                .andExpect(status().isForbidden());
    }
}

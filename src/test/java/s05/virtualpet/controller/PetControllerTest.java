package s05.virtualpet.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import s05.virtualpet.BaseIntegrationTest;
import s05.virtualpet.dto.UserLoginDTO;
import s05.virtualpet.dto.UserRegisterDTO;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Transactional
public class PetControllerTest extends BaseIntegrationTest {

    private String jwtToken;

    @BeforeEach
    void setup() throws Exception {
        String username = "petuser";
        String password = "Pet1234!";

        mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserRegisterDTO(username, password))))
                .andExpect(status().isOk());

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserLoginDTO(username, password))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map<?, ?> body = objectMapper.readValue(response, Map.class);
        jwtToken = "Bearer " + body.get("token");
    }

    @Test
    void testGetPetsReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/pets").header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testCreatePetAndRetrieve() throws Exception {
        mockMvc.perform(post("/pets")
                        .header("Authorization", jwtToken)
                        .contentType(APPLICATION_JSON)
                        .content("""
                    {
                      "name": "Nugget",
                      "type": "CLUBS"
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nugget"));

        mockMvc.perform(get("/pets")
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}

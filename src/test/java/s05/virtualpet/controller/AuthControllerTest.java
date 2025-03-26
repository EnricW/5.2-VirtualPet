package s05.virtualpet.controller;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import s05.virtualpet.BaseIntegrationTest;
import s05.virtualpet.dto.UserLoginDTO;
import s05.virtualpet.dto.UserRegisterDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;

@Transactional
public class AuthControllerTest extends BaseIntegrationTest {

    @Test
    void testUserCanRegisterAndLogin() throws Exception {
        var username = "authuser";
        var password = "Auth1234!";

        // Register
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserRegisterDTO(username, password))))
                .andExpect(status().isOk());

        // Login
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserLoginDTO(username, password))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}

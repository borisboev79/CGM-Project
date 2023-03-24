package cgm.controller;

import cgm.model.enums.BranchCode;
import cgm.model.enums.Role;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerIT {
    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser("admin")
    void testRegistration() throws Exception {
        mockMvc.perform(post("http://localhost:8080/users/register")
                        .param("id", "1")
                        .param("username", "kireto")
                        .param("password", "topsecret")
                        .param("firstName", "Kiril")
                        .param("lastName", "Lazarov")
                        .param("userRole", String.valueOf(List.of(Role.USER)))
                        .param("branch", BranchCode.SOFR.name())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"));


    }
}

package cgm.controller;

import cgm.model.dto.UserRegistrationDto;
import cgm.model.entity.BranchEntity;
import cgm.model.enums.BranchCode;
import cgm.model.enums.Role;


import cgm.repository.BranchRepository;
import cgm.repository.UserRepository;
import cgm.service.ApplicationUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetRegister() throws Exception {

        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("users-register"))
                .andExpect(model().attributeExists("adminRole"))
                .andExpect(model().attributeExists("managerRole"))
                .andExpect(model().attributeExists("userRole"))
                .andExpect(view().name("users-register"));
    }



    @Test
    @WithMockUser("admin")
    void testRegistrationWithError() throws Exception {

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

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testRegistration() throws Exception {

        UserRegistrationDto user = UserRegistrationDto.builder()
                .firstName("Kiril")
                .lastName("Lazarov")
                .username("kireto")
                .password("topsecret")
                .branch(BranchCode.HEAD)
                .roles(List.of(Role.USER))
                .build();


        mockMvc.perform(post("http://localhost:8080/users/register").flashAttr("userRegistrationDto", user)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));


    }

}

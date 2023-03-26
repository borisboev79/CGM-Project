package cgm.controller;

import cgm.model.dto.UserModificationDto;
import cgm.model.dto.UserRegistrationDto;
import cgm.model.entity.UserEntity;
import cgm.model.enums.BranchCode;
import cgm.model.enums.Role;
import cgm.repository.BranchRepository;
import cgm.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    private final String USERNAME = "kireto";
    private final String PASSWORD = "topsecret";
    private final String FIRST_NAME = "Kiril";
    private final String LAST_NAME = "Lazarov";
    private final String NEW_LASTNAME = "Karamelski";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

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
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .param("firstName", FIRST_NAME)
                        .param("lastName", LAST_NAME)
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
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .username(USERNAME)
                .password(PASSWORD)
                .branch(BranchCode.HEAD)
                .roles(List.of(Role.USER))
                .build();


        mockMvc.perform(post("http://localhost:8080/users/register").flashAttr("userRegistrationDto", user)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetUsersList() throws Exception {


        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allUsers"))
                .andExpect(view().name("users-all"));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testModifyUserGet() throws Exception {

        mockMvc.perform(get("/users/modify/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userModificationDto"))
                .andExpect(model().attributeExists("userBranch"))
                .andExpect(model().attributeExists("branches"))
                .andExpect(model().attributeExists("errorMsg"))
                .andExpect(model().attributeExists("adminRole"))
                .andExpect(model().attributeExists("managerRole"))
                .andExpect(model().attributeExists("userRole"))
                .andExpect(view().name("users-modify"));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testModifyUser() throws Exception {

        UserModificationDto userModificationDto = UserModificationDto.builder()
                .id(1L)
                .firstName(FIRST_NAME)
                .lastName(NEW_LASTNAME)
                .username("")
                .password(PASSWORD)
                .roles(List.of("USER"))
                .branch(BranchCode.HEAD)
                .build();


        mockMvc.perform(put("/users/modify/1").flashAttr("userModificationDto", userModificationDto)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/all"));

        UserEntity user = userRepository.findById(1L).orElseThrow();
        Assertions.assertEquals(user.getLastName(), NEW_LASTNAME);

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testModifyUserError() throws Exception {

        UserModificationDto userModificationDto = UserModificationDto.builder()
                .id(1L)
                .firstName("")
                .lastName(NEW_LASTNAME)
                .username(USERNAME)
                .password(PASSWORD)
                .roles(List.of("USER"))
                .branch(BranchCode.HEAD)
                .build();


        mockMvc.perform(put("/users/modify/1").flashAttr("userModificationDto", userModificationDto)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/errors/1"));

    }

}

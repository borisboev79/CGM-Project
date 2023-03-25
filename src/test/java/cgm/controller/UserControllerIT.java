package cgm.controller;

import cgm.model.dto.UserRegistrationDto;
import cgm.model.enums.BranchCode;
import cgm.model.enums.Role;
import cgm.repository.BranchRepository;
import cgm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    private final String EXISTING_USERNAME = "bocho";
    private final String EXISTING_PASSWORD = "topsecret";
    private final String EXISTING_FIRST_NAME = "Boris";
    private final String EXISTING_LAST_NAME = "Boev";
    private final String NON_EXISTING_USERNAME = "gosho";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
    /*List<RoleEntity> roles = new ArrayList<>();

    RoleEntity adminRole = new RoleEntity();
        adminRole.setRole(Role.ADMIN);
        adminRole.setDescription("Admin role");
        roles.add(adminRole);

    RoleEntity userRole = new RoleEntity();
        userRole.setRole(Role.USER);
        userRole.setDescription("User role");
        roles.add(userRole);


   UserEntity admin = UserEntity.builder().username(EXISTING_USERNAME).password(EXISTING_PASSWORD)
            .firstName(EXISTING_FIRST_NAME).lastName(EXISTING_LAST_NAME).roles(roles).build();

   userRepository.saveAndFlush(admin);

    UserDetails userDetails = userDetailsService.loadUserByUsername(EXISTING_USERNAME);

  */

    }

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

}

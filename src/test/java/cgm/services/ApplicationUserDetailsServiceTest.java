package cgm.services;

import cgm.model.entity.RoleEntity;
import cgm.model.entity.UserEntity;
import cgm.model.enums.Role;
import cgm.repository.UserRepository;
import cgm.service.ApplicationUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationUserDetailsServiceTest {

    private final String EXISTING_USERNAME = "admin";
    private final String NON_EXISTING_USERNAME = "gosho";
    private ApplicationUserDetailsService toTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp(){
        toTest = new ApplicationUserDetailsService(mockUserRepository);

    }

    @Test
    void testUserFound(){

        List<RoleEntity> roles = new ArrayList<>();

        RoleEntity adminRole = new RoleEntity();
        adminRole.setRole(Role.ADMIN);
        adminRole.setDescription("Admin role");
        roles.add(adminRole);

        RoleEntity userRole = new RoleEntity();
        userRole.setRole(Role.USER);
        userRole.setDescription("User role");
        roles.add(userRole);


        UserEntity testUserEntity = UserEntity.builder().username(EXISTING_USERNAME).password("topsecret")
                        .firstName("Boris").lastName("Boev").roles(roles).build();

        when(mockUserRepository.findUserEntityByUsername("admin")).thenReturn(Optional.of(testUserEntity));
        UserDetails adminDetails = toTest.loadUserByUsername("admin");

        Assertions.assertNotNull(adminDetails);
        Assertions.assertEquals(EXISTING_USERNAME, adminDetails.getUsername());
        Assertions.assertEquals("topsecret", adminDetails.getPassword());

        Assertions.assertEquals(2, adminDetails.getAuthorities().size());
        Assertions.assertEquals(roles.get(0).getRole(), Role.ADMIN);
        Assertions.assertEquals(roles.get(1).getRole(), Role.USER);


    }

    @Test
    void testUserNotFount(){
        assertThrows(UsernameNotFoundException.class, () -> {
            toTest.loadUserByUsername(NON_EXISTING_USERNAME);
        });
    }
}

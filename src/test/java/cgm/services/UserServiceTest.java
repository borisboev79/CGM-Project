package cgm.services;

import cgm.model.dto.UserModificationDto;
import cgm.model.dto.UserRegistrationDto;
import cgm.model.entity.UserEntity;
import cgm.repository.BranchRepository;
import cgm.repository.RoleRepository;
import cgm.repository.UserRepository;
import cgm.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final String EXISTING_USERNAME = "admin";
    private final String RAW_PASSWORD = "topsecret";
    private final String ENCODED_PASSWORD = "topsecret";

    private final String FIRST_NAME = "Boris";
    private final String LAST_NAME = "Boev";
    private final String NON_EXISTING_USERNAME = "gosho";

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private BranchRepository mockBranchRepository;

    @Mock
    private RoleRepository mockRoleRepository;

    @Mock
    private ModelMapper mockMapper;

    @Captor
    private ArgumentCaptor<UserEntity> userEntityArgumentCaptor;

    private UserService toTest;

    @BeforeEach
    void setUp(){
        toTest = new UserService(mockUserRepository, mockBranchRepository, mockRoleRepository, mockPasswordEncoder, mockMapper);
    }

    @Test
    void testUserRegistration(){

        UserRegistrationDto testRegistrationDto = new UserRegistrationDto();
        testRegistrationDto.setFirstName(FIRST_NAME);
        testRegistrationDto.setLastName(LAST_NAME);
        testRegistrationDto.setUsername(EXISTING_USERNAME);
        testRegistrationDto.setPassword(mockPasswordEncoder.encode(RAW_PASSWORD));

        when(mockPasswordEncoder.encode(testRegistrationDto.getPassword())).thenReturn(ENCODED_PASSWORD);

        toTest.registerUser(testRegistrationDto);

        Mockito.verify(mockUserRepository).save(userEntityArgumentCaptor.capture());
        assertEquals(FIRST_NAME, userEntityArgumentCaptor.getValue().getFirstName());


}

    @Test
    void testUserModification(){
        UserModificationDto testModificationDto = new UserModificationDto();
        testModificationDto.setFirstName(FIRST_NAME);
        testModificationDto.setLastName(LAST_NAME);
        testModificationDto.setUsername(NON_EXISTING_USERNAME);
        testModificationDto.setPassword(RAW_PASSWORD);

        when(mockPasswordEncoder.encode(testModificationDto.getPassword())).thenReturn(ENCODED_PASSWORD);
    }
}

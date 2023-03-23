package cgm.services;

import cgm.model.dto.UserModificationDto;
import cgm.model.dto.UserRegistrationDto;
import cgm.model.entity.BranchEntity;
import cgm.model.entity.RoleEntity;
import cgm.model.entity.UserEntity;
import cgm.model.enums.BranchCode;
import cgm.model.enums.Role;
import cgm.repository.BranchRepository;
import cgm.repository.RoleRepository;
import cgm.repository.UserRepository;
import cgm.service.UserService;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final String NEW_USERNAME = "petrov";
    private final String RAW_PASSWORD = "topsecret";
    private final String ENCODED_PASSWORD = "%($)GGPPP3fdfd";

    private final Long VALID_ID = 1L;
    private final Long INVALID_ID = 300L;

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

    private UserService testUserService;

    private BranchEntity testBranchEntity;

    private RoleEntity testRoleEntity;

    private UserRegistrationDto testRegistrationDto;

    @BeforeEach
    void setUp(){
        testUserService = new UserService(mockUserRepository, mockBranchRepository, mockRoleRepository, mockPasswordEncoder, mockMapper);

        testBranchEntity = BranchEntity.builder()
                .name("Head Office")
                .code(BranchCode.HEAD)
                .address("Sofia")
                .email("head@sofia.bg")
                .build();

        testRoleEntity = new RoleEntity();
        testRoleEntity.setRole(Role.USER);
        testRoleEntity.setDescription("User");

        testRegistrationDto = new UserRegistrationDto();
        testRegistrationDto.setFirstName(FIRST_NAME);
        testRegistrationDto.setLastName(LAST_NAME);
        testRegistrationDto.setUsername(NEW_USERNAME);
        testRegistrationDto.setPassword(RAW_PASSWORD);
        testRegistrationDto.setBranch(BranchCode.HEAD);
        testRegistrationDto.setRoles(List.of(Role.USER));
        testRegistrationDto.setId(VALID_ID);

        lenient().when(mockPasswordEncoder.encode(testRegistrationDto.getPassword())).thenReturn(ENCODED_PASSWORD);
        lenient().when(mockBranchRepository.findBranchEntityByCode(testRegistrationDto.getBranch())).thenReturn(Optional.of(testBranchEntity));
        lenient().when(mockRoleRepository.findRoleEntityByRole(testRegistrationDto.getRoles().get(0))).thenReturn(Optional.of(testRoleEntity));


        //Ако тук създадем някакъв обект, той трябва задължително да се използва във всички тествани методи по-долу. Ако това не стане, тестът ще гръмне
        // с Unecessary stubbings detected. За да избегнем това, преди такъв обект, който примерно ще ползваме все пак в повечето тестове, пишем
        // lenient().

    }

    @Test
    public void testGetUserById(){

        UserEntity savedUser = userEntityArgumentCaptor.getValue();



        when(testUserService.getAllUsers()).thenReturn(List.of(savedUser));

        testUserService.getAllUsers();


    }

    @Test
    void testUserRegistration(){

        testUserService.registerUser(testRegistrationDto);

        Mockito.verify(mockUserRepository).saveAndFlush(userEntityArgumentCaptor.capture());

        UserEntity savedUser = userEntityArgumentCaptor.getValue();
        assertEquals(FIRST_NAME, savedUser.getFirstName());
        assertEquals(ENCODED_PASSWORD, savedUser.getPassword());
        assertEquals(1, savedUser.getRoles().size());


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

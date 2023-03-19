package cgm.service;

import cgm.model.dto.UserRegistrationDto;
import cgm.model.dto.UserViewDto;
import cgm.model.entity.BranchEntity;
import cgm.model.entity.RoleEntity;
import cgm.model.entity.UserEntity;
import cgm.model.enums.BranchCode;
import cgm.repository.BranchRepository;
import cgm.repository.RoleRepository;
import cgm.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper mapper;

    @Autowired
    public UserService(UserRepository userRepository, BranchRepository branchRepository,
                       RoleRepository roleRepository, PasswordEncoder encoder,
                       ModelMapper mapper) {
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        this.roleRepository = roleRepository;

        this.encoder = encoder;
        this.mapper = mapper;
    }

    public void registerUser(UserRegistrationDto userRegistrationDto) {

        List<RoleEntity> roles = userRegistrationDto.getRoles()
                .stream()
                .map(role -> this.roleRepository.findRoleEntityByRole(role).orElseThrow())
                .toList();

        BranchEntity branch = this.branchRepository.findBranchEntityByCode(BranchCode.valueOf(userRegistrationDto.getBranch().name()))
                .orElse(null);

        UserEntity user = UserEntity.builder()
                .username(userRegistrationDto.getUsername())
                .password(encoder.encode(userRegistrationDto.getPassword()))
                .firstName(userRegistrationDto.getFirstName())
                .lastName(userRegistrationDto.getLastName())
                .branch(branch)
                .roles(roles)
                .build();

        userRepository.saveAndFlush(user);

    }

    public List<UserViewDto> getAllUsers() {

        List<UserViewDto> userViews = new ArrayList<>();

        this.userRepository.findAll().forEach(user -> {

            UserViewDto userViewDto = new UserViewDto();

            mapper.map(user, userViewDto);
            userViewDto.setRoles(user.getRoles()
                    .stream()
                    .map(RoleEntity::toString)
                    .collect(Collectors.joining(", ")));

            userViews.add(userViewDto);
        });

        return userViews;
    }
}

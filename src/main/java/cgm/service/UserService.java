package cgm.service;

import cgm.model.dto.UserRegistrationDto;
import cgm.model.entity.BranchEntity;
import cgm.model.entity.RoleEntity;
import cgm.model.entity.UserEntity;
import cgm.model.enums.BranchCode;
import cgm.repository.BranchRepository;
import cgm.repository.RoleRepository;
import cgm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

 //   private final UserDetailsService userDetailsService;


    @Autowired
    public UserService(UserRepository userRepository, BranchRepository branchRepository, RoleRepository roleRepository, PasswordEncoder encoder
            //, UserDetailsService userDetailsService
    ) {
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;

    //    this.userDetailsService = userDetailsService;

    }

/*
    public UserEntity getUserByUsername(String username) {
        return this.userRepository.findUserEntityByUsername(username).orElseThrow();
    }
*/


    public void registerUser(UserRegistrationDto userRegistrationDto){

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

    /*public void registerUser(UserRegistrationDto userRegistrationDto,
                             Consumer<Authentication> successfulLoginProcessor) {

        List<RoleEntity> roles = userRegistrationDto.getRoles()
                .stream()
                .map(role -> this.roleRepository.findRoleEntityByRole(role).orElseThrow())
                .toList();

        BranchEntity branch = this.branchRepository.findBranchEntityByCode(BranchCode.valueOf(userRegistrationDto.getBranch().name()))
                .orElseThrow(NullPointerException::new);

        UserEntity user = UserEntity.builder()
                .username(userRegistrationDto.getUsername())
                .password(encoder.encode(userRegistrationDto.getPassword()))
                .firstName(userRegistrationDto.getFirstName())
                .lastName(userRegistrationDto.getLastName())
                .branch(branch)
                .roles(roles)
                .build();

        userRepository.saveAndFlush(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userRegistrationDto.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        successfulLoginProcessor.accept(authentication);
    }*/


}

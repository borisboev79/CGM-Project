package cgm.service;

import cgm.model.dto.UserRegistrationDto;
import cgm.model.entity.BranchEntity;
import cgm.model.entity.RoleEntity;
import cgm.model.entity.UserEntity;
import cgm.model.enums.BranchCode;
import cgm.repository.BranchRepository;
import cgm.repository.RoleRepository;
import cgm.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    private final UserDetailsService userDetailsService;


    @Autowired
    public UserService(UserRepository userRepository, BranchRepository branchRepository, RoleRepository roleRepository, PasswordEncoder encoder, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;

        this.userDetailsService = userDetailsService;

    }

    public UserEntity getUserByUsername(String username) {
        return this.userRepository.findUserEntityByUsername(username).orElseThrow();
    }


    public void registerUser(UserRegistrationDto userRegistrationDto,
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
    }


}

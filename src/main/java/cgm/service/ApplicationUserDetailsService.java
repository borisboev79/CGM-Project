package cgm.service;

import cgm.model.CurrentUser;
import cgm.model.entity.RoleEntity;
import cgm.model.entity.UserEntity;
import cgm.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public ApplicationUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return
                userRepository.
                        findUserEntityByUsername(username).
                        map(this::userDetailsMap).
                        orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " not found!"));
    }

    private UserDetails userDetailsMap(UserEntity userEntity) {
        return new CurrentUser(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getRoles().stream().map(this::mapRole).toList())
                .setFirstName(userEntity.getFirstName())
                .setLastName(userEntity.getLastName())
                .setBranch(userEntity.getBranch());
    }

    private GrantedAuthority mapRole(RoleEntity roleEntity) {
        return new SimpleGrantedAuthority("ROLE_" + roleEntity.getRole().name());
    }
}

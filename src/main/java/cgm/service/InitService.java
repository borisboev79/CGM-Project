package cgm.service;

import cgm.model.entity.BranchEntity;
import cgm.model.entity.RoleEntity;
import cgm.model.entity.UserEntity;
import cgm.model.enums.BranchCode;
import cgm.model.enums.Role;
import cgm.repository.BranchRepository;
import cgm.repository.RoleRepository;
import cgm.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InitService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    public InitService(RoleRepository roleRepository,
                       UserRepository userRepository,
                       BranchRepository branchRepository,
                       PasswordEncoder passwordEncoder,
                       @Value("${app.default.password}") String defaultPassword) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        initRoles();
        initBranches();
        initUsers();
    }


    private void initRoles() {
        if (roleRepository.count() == 0) {
            List<RoleEntity> roles = List.of(
                    RoleEntity.builder().role(Role.MANAGER).description("Branch managers can add users.").build(),
                    RoleEntity.builder().role(Role.ADMIN).description("Admins can add groups, users and all.").build(),
                    RoleEntity.builder().role(Role.USER).description("Users can only add guests to groups.").build()
            );

            this.roleRepository.saveAllAndFlush(roles);
        }
    }

    private void initBranches() {
        if (branchRepository.count() == 0) {
            List<BranchEntity> branches = List.of(
                    BranchEntity.builder().name("Head Office").code(BranchCode.HEAD).address("София, пл. Папа Йоан Павел II №1, ет.7").email("products@usitcolours.bg").build(),
                    BranchEntity.builder().name("Blagoevgrad").code(BranchCode.BLGD).address("Благоевград, ул. Крали Марко 1").email("blgd@usitcolours.bg").build(),
                    BranchEntity.builder().name("Burgas").code(BranchCode.BUR).address("Бургас, ул. Цар Петър 20 (до Операта)").email("burgas@usitcolours.bg").build(),
                    BranchEntity.builder().name("Sofia central").code(BranchCode.SOFR).address("София, бул. Васил Левски №35").email("sofia@usitcolours.bg").build()
            );
            this.branchRepository.saveAllAndFlush(branches);
        }


    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            initAdmin();
            initManager();
            initNormalUser();
        }
    }

    private void initAdmin() {
        UserEntity adminUser = UserEntity.builder()
                .username("admin")
                .password(passwordEncoder.encode("topsecret"))
                .firstName("Boris")
                .lastName("Boev")
                .branch(this.branchRepository.findByName("Head office").orElseThrow())
                .roles(this.roleRepository.findAll()).build();

        this.userRepository.save(adminUser);
    }

    private void initManager() {

        var managerRole = this.roleRepository.
                findRoleEntityByRole(Role.MANAGER).orElseThrow();

        UserEntity managerUser = UserEntity.builder()
                .username("manager")
                .password(passwordEncoder.encode("topsecret"))
                .firstName("Boryana")
                .lastName("Veselinova")
                .branch(this.branchRepository.findByName("Sofia central").orElseThrow())
                .roles(List.of(managerRole))
                .build();

        this.userRepository.save(managerUser);
    }

    private void initNormalUser() {

        var userRole = this.roleRepository.
                findRoleEntityByRole(Role.USER).orElseThrow();

        UserEntity normalUser = UserEntity.builder()
                .username("user")
                .password(passwordEncoder.encode("topsecret"))
                .firstName("Ivaylo")
                .lastName("Ivanov")
                .branch(this.branchRepository.findByName("Sofia central").orElseThrow())
                .roles(List.of(userRole))
                .build();

        this.userRepository.save(normalUser);
    }
}


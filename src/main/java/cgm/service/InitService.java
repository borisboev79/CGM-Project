package cgm.service;

import cgm.model.entity.*;
import cgm.model.enums.BranchCode;
import cgm.model.enums.Role;
import cgm.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InitService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;
    private final CruiseLineRepository cruiseLineRepository;
    private final ShipRepository shipRepository;

    public InitService(RoleRepository roleRepository,
                       UserRepository userRepository,
                       BranchRepository branchRepository,
                       PasswordEncoder passwordEncoder,
                      // @Value("${app.default.password}") String defaultPassword,
                       CruiseLineRepository cruiseLineRepository,
                       ShipRepository shipRepository) {

        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        this.passwordEncoder = passwordEncoder;
        this.cruiseLineRepository = cruiseLineRepository;
        this.shipRepository = shipRepository;
    }

    @PostConstruct
    public void init() {
        initRoles();
        initBranches();
        initUsers();
        initCruiseLines();
        initShips();
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

    private void initCruiseLines() {
        long count = this.cruiseLineRepository.count();
        if (count == 0) {

            List<CruiseLine> cruiseLines = List.of(
                    CruiseLine.builder().name("MSC").logoUrl("https://i.pinimg.com/736x/d3/94/bc/d394bc6b6e668a4f6c9c22c1721e4478.jpg").build(),
                    CruiseLine.builder().name("Costa").logoUrl("https://cruise.jobs/uploads/images/costa-cruises-logo.png").build(),
                    CruiseLine.builder().name("Celestyal").logoUrl("https://image.center.cruises/image/00000000-0000-0000-0000-056287334995.png").build()
            );

            this.cruiseLineRepository.saveAllAndFlush(cruiseLines);
        }
    }

    private void initShips() {
        if (this.shipRepository.count() == 0) {
            createShips("MSC", "MSC Fantasia", "MSC Sinfonia", "MSC Opera", "MSC Preziosa");
            createShips("Costa", "Costa Toscana", "Costa Smeralda", "Costa Pacifica", "Costa Fascinosa");
            createShips("Celestyal", "Celestyal Olympia", "Celestyal Crystal");
        }
    }
    private void createShips(String cruiseLine, String... ships) {
        CruiseLine line = this.cruiseLineRepository.findByName(cruiseLine).orElseThrow();
        List<Ship> cruiseShips = new ArrayList<>();

        for (String ship : ships) {
            cruiseShips.add(Ship.builder().name(ship).cruiseLine(line).build());
        }
        this.shipRepository.saveAllAndFlush(cruiseShips);
        line.setShips(this.shipRepository.findAllByCruiseLine_Name(cruiseLine).orElse(null));

    }

}


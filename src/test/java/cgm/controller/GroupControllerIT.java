package cgm.controller;

import cgm.model.dto.GroupAddDto;
import cgm.model.entity.*;
import cgm.model.enums.BranchCode;
import cgm.model.enums.Role;
import cgm.model.enums.Transportation;
import cgm.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GroupControllerIT {

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private CruiseLineRepository cruiseLineRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private GroupRepository groupRepository;


    @Autowired
    private MockMvc mockMvc;

    public GroupControllerIT() {
    }

    @Test
    public void testGetAllGroups() throws Exception {

        mockMvc.perform(get("/groups/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("groups"))
                .andExpect(view().name("groups"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "MANAGER", "ADMIN"})
    public void testGetGroupAdd() throws Exception {

        mockMvc.perform(get("/groups/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cruiselines", "transportation", "groupAddDto"))
                .andExpect(view().name("group-add"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAddGroup() throws Exception {

        CruiseLine cruiseLine = CruiseLine.builder().name("MSC Cruises").build();

        cruiseLineRepository.saveAndFlush(cruiseLine);

        Ship ship = Ship.builder()
                .name("MSC Aronia")
                .cruiseLine(cruiseLine)
                .build();

        shipRepository.saveAndFlush(ship);

        GroupAddDto group = new GroupAddDto();
        group.setShip("MSC Aronia");
        group.setName("gfklgkgkgkgkgkgff");
        group.setStartDate(LocalDate.now());
        group.setEndDate(LocalDate.now().plusDays(7));
        group.setImageUrl("imgUrl/image.jpg");
        group.setItinerary("qwertyuuiopalfg");
        group.setTotalPax(12);
        group.setTransportation(Transportation.BUS);

        mockMvc.perform(post("/groups/add").flashAttr("groupAddDto", group)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cabins/add/3"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "MANAGER", "ADMIN"})
    public void testGetDetails() throws Exception {


        mockMvc.perform(get("/groups/details/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cruiseGroup"))
                .andExpect(view().name("group-details"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "MANAGER", "ADMIN"})
    public void testGetGroupsDelete() throws Exception {

        mockMvc.perform(get("/groups/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("groups"))
                .andExpect(view().name("groups-delete"));
    }



    @Test
    public void testDeleteGroupFail() throws Exception {

        mockMvc.perform(get("/groups/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteGroup() throws Exception {

                RoleEntity role = new RoleEntity();
        role.setRole(Role.ADMIN);
        role.setDescription("Admin");

        roleRepository.saveAndFlush(role);

        BranchEntity branch = BranchEntity.builder()
                .name("Blagoevgrad")
                .code(BranchCode.BLGD)
                .address("Blagoevgrad")
                .email("blgd@sofia.bg")
                .build();

        branchRepository.saveAndFlush(branch);


        UserEntity user = UserEntity.builder()
                .username("petarcho")
                .password("topsecret")
                .firstName("Petar")
                .lastName("Petrov")
                .roles(List.of(role))
                .branch(branch)
                .build();

        userRepository.saveAndFlush(user);


        CruiseLine cruiseLine = CruiseLine.builder().name("Costa Cruises").build();

        cruiseLineRepository.saveAndFlush(cruiseLine);

        Ship ship = Ship.builder()
                .name("Costa Cosmia")
                .cruiseLine(cruiseLine)
                .build();

        shipRepository.saveAndFlush(ship);


        CruiseGroup group2 = CruiseGroup.builder()
                .name("GGJGJJGJGJGJGJGJG")
                .duration(7L)
                .startDate(Instant.now())
                .endDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .itinerary("LLKLKLLKLKLKLKLKLKLKLKL")
                .totalPax(24)
                .ship(ship)
                .employee(user)
                .transportation(Transportation.BUS)
                .imageUrl("ggogogog.gi/ififi.jpg")
                .build();

        groupRepository.saveAndFlush(group2);

        group2.setId(2L);

        groupRepository.saveAndFlush(group2);

        mockMvc.perform(get("/groups/delete/2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/delete"));
    }
}
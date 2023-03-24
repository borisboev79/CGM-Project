package cgm.controller;

import cgm.model.dto.GroupAddDto;
import cgm.model.entity.CruiseLine;
import cgm.model.entity.Ship;
import cgm.model.enums.Transportation;
import cgm.repository.CruiseLineRepository;
import cgm.repository.ShipRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

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
    @WithMockUser(username = "admin", roles={"USER", "MANAGER","ADMIN"})
    public void testGetGroupAdd() throws Exception {

        mockMvc.perform(get("/groups/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cruiselines", "transportation", "groupAddDto"))
                .andExpect(view().name("group-add"));
    }

    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
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
                .andExpect(redirectedUrl("/cabins/add/1"));
    }

    @Test
    @WithMockUser(username = "admin", roles={"USER", "MANAGER","ADMIN"})
    public void testGetDetails() throws Exception {


        mockMvc.perform(get("/groups/details/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cruiseGroup"))
                .andExpect(view().name("group-details"));
    }

    @Test
    @WithMockUser(username = "admin", roles={"USER", "MANAGER","ADMIN"})
    public void testGetGroupsDelete() throws Exception {

        mockMvc.perform(get("/groups/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("groups"))
                .andExpect(view().name("groups-delete"));
    }

    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
    public void testDeleteGroup() throws Exception {

        mockMvc.perform(get("/groups/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/all"));
    }

    @Test
    public void testDeleteGroupFail() throws Exception {

        mockMvc.perform(get("/groups/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
}
}
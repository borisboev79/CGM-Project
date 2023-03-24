package cgm.controller;

import cgm.model.dto.GroupAddDto;
import cgm.model.dto.GroupViewDto;
import cgm.model.dto.UserViewDto;
import cgm.model.entity.CruiseGroup;
import cgm.model.entity.CruiseLine;
import cgm.model.enums.Transportation;
import cgm.service.CruiseLineService;
import cgm.service.GroupService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

public class GroupControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private GroupService groupService;

    @Mock
    private CruiseLineService cruiseLineService;

    @Test
    public void testGetAllGroups() throws Exception {

        List<GroupViewDto> groups = new ArrayList<>();

        GroupViewDto group = new GroupViewDto();
        group.setId(1L);
        group.setDuration(7L);
        group.setStartDate(Instant.now());
        group.setEndDate(Instant.now().plus(7, ChronoUnit.DAYS));
        group.setImageUrl("imgUrl/image.jpg");
        group.setItinerary("qwertyuuiopalfg");
        group.setTotalPax(12);
        group.setShipName("Ship");
        group.setEmployee(new UserViewDto());
        group.setTransportation(Transportation.BUS);
        group.setSoldPax(0);
        group.setSoldOut(false);


        groups.add(group);
        when(groupService.getAllGroups()).thenReturn(groups);


        mockMvc.perform(get("/groups/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("groups"))
                .andExpect(view().name("groups"));
    }

    @Test
    @WithMockUser(username = "admin", roles={"USER", "MANAGER","ADMIN"})
    public void testGetGroupAdd() throws Exception {
        // Mock the service layer to return a list of CruiseLine objects
        List<CruiseLine> cruiselines = new ArrayList<>();
        cruiselines.add(new CruiseLine());
        when(cruiseLineService.getCruiseLines()).thenReturn(cruiselines);


        mockMvc.perform(get("/groups/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cruiselines", "transportation", "groupAddDto"))
                .andExpect(view().name("group-add"));
    }

    @Test
    @WithMockUser(username = "admin", roles={"USER", "MANAGER","ADMIN"})
    public void testAddGroup() throws Exception {
        // Mock the service layer to return a CruiseGroup object
        CruiseGroup group = new CruiseGroup();
        group.setId(1L);
        when(groupService.createGroup(any(GroupAddDto.class), anyString())).thenReturn(group);


        mockMvc.perform(post("/groups/add")
                        .param("name", "Group Name")
                        .param("description", "Group Description")
                        .param("cruiseLineId", "1")
                        .param("departurePort", "Port")
                        .param("destinationPort", "Port")
                        .param("transportation", "SHIP")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/add/cabins/1"));
    }

    @Test
    @WithMockUser(username = "admin", roles={"USER", "MANAGER","ADMIN"})
    public void testGetDetails() throws Exception {

        GroupViewDto group = new GroupViewDto();
        group.setId(1L);
        when(groupService.findById(1L)).thenReturn(group);


        mockMvc.perform(get("/groups/details/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cruiseGroup"))
                .andExpect(view().name("group-details"));
    }

    @Test
    @WithMockUser(username = "admin", roles={"USER", "MANAGER","ADMIN"})
    public void testGetGroupsDelete() throws Exception {

        List<GroupViewDto> groups = new ArrayList<>();
        groups.add(new GroupViewDto());
        when(groupService.getAllGroups()).thenReturn(groups);


        mockMvc.perform(get("/groups/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("groups"))
                .andExpect(view().name("groups-delete"));
    }

    @Test
    @WithMockUser(username = "admin", roles={"USER", "MANAGER","ADMIN"})
    public void testDeleteGroup() throws Exception {
        // Make a GET request to /groups/delete/{id}
        mockMvc.perform(get("/groups/delete/1")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/all"));
    }

    @Test
    public void testDeleteGroupFail() throws Exception {
        // Make a GET request to /groups/delete/{id}
        mockMvc.perform(get("/groups/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
}
}
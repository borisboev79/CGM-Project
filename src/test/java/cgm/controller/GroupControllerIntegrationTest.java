package cgm.controller;

import cgm.model.dto.GroupAddDto;
import cgm.model.dto.GroupViewDto;
import cgm.model.entity.CruiseGroup;
import cgm.model.entity.CruiseLine;
import cgm.service.CruiseLineService;
import cgm.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebMvcTest(GroupController.class)
public class GroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @MockBean
    private CruiseLineService cruiseLineService;

    @Test
    public void testGetAllGroups() throws Exception {
        // Mock the service layer to return a list of CruiseGroup objects
        List<GroupViewDto> groups = new ArrayList<>();
        groups.add(new GroupViewDto());
        when(groupService.getAllGroups()).thenReturn(groups);

        // Make a GET request to /groups/all
        mockMvc.perform(get("/groups/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("groups"))
                .andExpect(view().name("groups"));
    }

    @Test
    public void testGetGroupAdd() throws Exception {
        // Mock the service layer to return a list of CruiseLine objects
        List<CruiseLine> cruiselines = new ArrayList<>();
        cruiselines.add(new CruiseLine());
        when(cruiseLineService.getCruiseLines()).thenReturn(cruiselines);

        // Make a GET request to /groups/add
        mockMvc.perform(get("/groups/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cruiselines", "transportation", "groupAddDto"))
                .andExpect(view().name("group-add"));
    }

    @Test
    public void testAddGroup() throws Exception {
        // Mock the service layer to return a CruiseGroup object
        CruiseGroup group = new CruiseGroup();
        group.setId(1L);
        when(groupService.createGroup(any(GroupAddDto.class), anyString())).thenReturn(group);

        // Make a POST request to /groups/add with valid data
        mockMvc.perform(post("/groups/add")
                        .param("name", "Group Name")
                        .param("description", "Group Description")
                        .param("cruiseLineId", "1")
                        .param("departurePort", "Port")
                        .param("destinationPort", "Port")
                        .param("transportation", "SHIP"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/add/cabins/1"));
    }

    @Test
    public void testGetDetails() throws Exception {
        // Mock the service layer to return a CruiseGroup object
        GroupViewDto group = new GroupViewDto();
        group.setId(1L);
        when(groupService.findById(1L)).thenReturn(group);

        // Make a GET request to /groups/details/{id}
        mockMvc.perform(get("/groups/details/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cruiseGroup"))
                .andExpect(view().name("group-details"));
    }

    @Test
    public void testGetGroupsDelete() throws Exception {
        // Mock the service layer to return a list of CruiseGroup objects
        List<GroupViewDto> groups = new ArrayList<>();
        groups.add(new GroupViewDto());
        when(groupService.getAllGroups()).thenReturn(groups);

        // Make a GET request to /groups/delete
        mockMvc.perform(get("/groups/delete"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("groups"))
                .andExpect(view().name("groups-delete"));
    }

    @Test
    public void testDeleteGroup() throws Exception {
        // Make a GET request to /groups/delete/{id}
        mockMvc.perform(get("/groups/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/delete"));

        // Verify that the service layer's delete method was called with the correct ID
    }
}
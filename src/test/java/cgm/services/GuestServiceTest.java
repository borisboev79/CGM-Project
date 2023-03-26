package cgm.services;

import cgm.model.dto.GuestAddDto;
import cgm.model.entity.*;
import cgm.model.enums.BranchCode;
import cgm.model.enums.CabinType;
import cgm.model.enums.Role;
import cgm.model.enums.Transportation;
import cgm.repository.CabinRepository;
import cgm.repository.GroupRepository;
import cgm.repository.GuestRepository;
import cgm.service.GuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GuestServiceTest {

    private final Long COMMON_ID = 1L;

    @Mock
    private GuestRepository testGuestRepository;
    @Mock
    private CabinRepository testCabinRepository;
    @Mock
    private GroupRepository testGroupRepository;
    @Mock
    private ModelMapper testMapper;
    @InjectMocks
    private GuestService testGuestService;
    @Captor
    private ArgumentCaptor<Guest> guestArgumentCaptor;

    @Captor
    private ArgumentCaptor<Cabin> cabinArgumentCaptor;

    @Captor
    private ArgumentCaptor<CruiseGroup> groupArgumentCaptor;

    private RoleEntity role;

    private BranchEntity branch;

    private UserEntity user;

    private CruiseLine cruiseLine;

    private Ship ship;

    private CruiseGroup group;

    private Cabin cabin;


    private Guest guest;

    private GuestAddDto guestAddDto;


    @BeforeEach
    public void setUp() {

        cruiseLine = CruiseLine.builder().name("MSC Cruises").build();

        ship = Ship.builder()
                .name("MSC Aronia")
                .cruiseLine(cruiseLine)
                .build();
        ship.setId(COMMON_ID);


        role = RoleEntity.builder()
                .role(Role.USER)
                .description("User")
                .build();
        role.setId(COMMON_ID);


        branch = BranchEntity.builder()
                .name("Head Office")
                .code(BranchCode.HEAD)
                .address("Sofia")
                .email("head@sofia.bg")
                .build();
        branch.setId(COMMON_ID);


        user = UserEntity.builder()
                .firstName("Boris")
                .lastName("Boev")
                .username("admin")
                .password("topsecret")
                .branch(branch)
                .roles(List.of(role))
                .build();
        user.setId(COMMON_ID);


        group = CruiseGroup.builder()
                .ship(ship)
                .name("gfklgkgkgkgkgkgff")
                .startDate(Instant.now())
                .endDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .imageUrl("imgUrl/image.jpg")
                .itinerary("qwertyuuiopalfg")
                .totalPax(12)
                .duration(7L)
                .transportation(Transportation.BUS)
                .employee(user)
                .build();
        group.setId(COMMON_ID);


        cabin = Cabin.builder()
                .cabinCode("IA")
                .type(CabinType.INSIDE)
                .maxOccupancy(1)
                .adultPrice(199.00)
                .extraAdultPrice(155.00)
                .childPrice(99.00)
                .ship(ship)
                .cruiseGroup(group)
                .paxNumber(0)
                .totalPrice(0.0)
                .isFull(false)
                .addedBy(branch)
                .build();
        cabin.setId(COMMON_ID);

        guestAddDto = GuestAddDto.builder()
                .fullName("Penka Ivanova")
                .email("penka@abv.bg")
                .birthDate(LocalDate.now().minusYears(33))
                .phone("123456789")
                .passportNumber("123456789")
                .EGN("9912129999")
                .build();

        guest = Guest.builder()
                .fullName("Penka Ivanova")
                .email("penka@abv.bg")
                .birthDate(Instant.now().minus(7500, ChronoUnit.DAYS))
                .phone("123456789")
                .passportNumber("123456789")
                .EGN("9912129999")
                .build();
        guest.setId(COMMON_ID);


        when(testCabinRepository.findById(COMMON_ID)).thenReturn(Optional.of(cabin));
        when(testGroupRepository.findById(cabin.getId())).thenReturn(Optional.of(group));
        when(testMapper.map(guestAddDto, Guest.class)).thenReturn(guest);

        //thenCallRealMethod();

    }

    @Test
    public void testAddGuest() {

        this.testGuestService.addGuest(guestAddDto, 1L);

        Mockito.verify(testGuestRepository).saveAndFlush(guestArgumentCaptor.capture());
        Mockito.verify(testCabinRepository).saveAndFlush(cabinArgumentCaptor.capture());
        Mockito.verify(testGroupRepository).saveAndFlush(groupArgumentCaptor.capture());

        Guest addedGuest = guestArgumentCaptor.getValue();
        Cabin addedCabin = cabinArgumentCaptor.getValue();
        CruiseGroup addedGroup = groupArgumentCaptor.getValue();


        assertEquals("123456789", addedGuest.getPhone());
        assertEquals("penka@abv.bg", addedGuest.getEmail());
        assertEquals("9912129999", addedGuest.getEGN());
        assertEquals(COMMON_ID, addedGuest.getId());
        assertEquals(user.getBranch(), addedGuest.getCabin().getAddedBy());

        assertEquals(1, addedGroup.getSoldPax());

        assertEquals(199.00, addedCabin.getTotalPrice());


    }

    @Test
    public void testEditGuest() {


        this.testGuestService.addGuest(guestAddDto, 1L);


    }


}


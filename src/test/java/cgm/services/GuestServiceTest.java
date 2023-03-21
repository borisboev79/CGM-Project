package cgm.services;

import cgm.model.entity.Guest;
import cgm.model.entity.UserEntity;
import cgm.repository.CabinRepository;
import cgm.repository.GroupRepository;
import cgm.repository.GuestRepository;
import cgm.service.GuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
public class GuestServiceTest {

    @Mock
    private Guest testGuest;

    @Mock
    private GuestRepository testGuestRepository;
    @Mock
    private CabinRepository testCabinRepository;

    @Mock
    private GroupRepository testGroupRepository;

    @Mock
    private ModelMapper testMapper;

    @Captor
    private ArgumentCaptor<Guest> guestArgumentCaptor;

    private GuestService testGuestService;

    @BeforeEach
    public void setUp() {

        this.testGuestService = new GuestService(testGuestRepository, testCabinRepository, testGroupRepository, testMapper);

    }

    @Test
    public void testAddGuest(){
        this.testGuest = Guest.builder()
                .fullName("Penka Ivanova")
                .email("penka@abv.bg")
                .birthDate(Instant.now())
                .phone("123456789")
                .passportNumber("123456789")
                .EGN("9912129999")
                .build();



    }


}


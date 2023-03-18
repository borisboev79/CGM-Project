package cgm.util;

import cgm.model.entity.CruiseGroup;
import cgm.repository.GroupRepository;
import cgm.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SoldGroupCheckScheduler {

    private final GroupRepository groupRepository;
    private final EmailService emailService;
    private final String CRUISE_ADMIN = "Cruise Group Manager";

    @Autowired
    public SoldGroupCheckScheduler(GroupRepository groupRepository, EmailService emailService) {
        this.groupRepository = groupRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 30 13,18 * * *")
    public void checkForSoldOutGroups() {

        List<CruiseGroup> soldGroups = this.groupRepository.findAll().stream()
                .filter(CruiseGroup::isSoldOut)
                .toList();


        if (!soldGroups.isEmpty()) {
            this.emailService.sendInfoEmail("products@usitcolours.bg", CRUISE_ADMIN, soldGroups);
        }

    }
}

package cgm.service;

import cgm.repository.GroupRepository;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void createGroup() {

    }
}

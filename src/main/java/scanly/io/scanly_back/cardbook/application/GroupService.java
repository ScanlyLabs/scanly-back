package scanly.io.scanly_back.cardbook.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import scanly.io.scanly_back.cardbook.domain.GroupRepository;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    
}

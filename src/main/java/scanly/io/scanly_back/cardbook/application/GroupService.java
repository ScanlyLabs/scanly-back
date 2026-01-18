package scanly.io.scanly_back.cardbook.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.cardbook.application.dto.command.CreateGroupCommand;
import scanly.io.scanly_back.cardbook.application.dto.info.GroupInfo;
import scanly.io.scanly_back.cardbook.domain.Group;
import scanly.io.scanly_back.cardbook.domain.GroupRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository groupRepository;


    /**
     * 신규 명함첩 그룹 생성
     * 1. 회원 아이디로 명함첩 그룹 수 조회
     * 2. 명함첩 그룹 저장
     * @param command 그룹 생성 정보
     * @return 신규 그룹
     */
    @Transactional
    public GroupInfo create(CreateGroupCommand command) {
        // 1. 회원 아이디로 명함첩 그룹 수 조회
        String memberId = command.memberId();
        long count = groupRepository.countByMemberId(memberId);

        // 2. 명함첩 그룹 저장
        Group group = Group.create(memberId, command.name(), (int) count);
        Group savedGroup = groupRepository.save(group);

        return GroupInfo.from(savedGroup);
    }
}

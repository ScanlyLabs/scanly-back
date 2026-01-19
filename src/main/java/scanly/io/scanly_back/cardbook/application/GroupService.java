package scanly.io.scanly_back.cardbook.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.cardbook.application.dto.command.CreateGroupCommand;
import scanly.io.scanly_back.cardbook.application.dto.command.RenameGroupCommand;
import scanly.io.scanly_back.cardbook.application.dto.command.ReorderGroupCommand;
import scanly.io.scanly_back.cardbook.application.dto.info.GroupInfo;
import scanly.io.scanly_back.cardbook.domain.Group;
import scanly.io.scanly_back.cardbook.domain.GroupRepository;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * 명함첩 그룹 단건 조회
     * @param id 명함첩 그룹 아이디
     * @return 조회된 명함첩 그룹
     */
    public Group findById(String id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
    }

    /**
     * 명함첩 그룹 단건 조회
     * @param id 명함첩 그룹 아이디
     * @param memberId 회원 아이디
     * @return 조회된 명함첩 그룹
     */
    public Group findByIdAndMemberId(String id, String memberId) {
        return groupRepository.findByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
    }

    /**
     * 명함첩 그룹명 변경
     * 1. 그룹 조회
     * 2. 그룹명 변경
     * @param command 수정할 명함첩 정보
     * @return 수정된 그룹 정보
     */
    @Transactional
    public GroupInfo rename(RenameGroupCommand command) {
        String id = command.id();

        // 1. 그룹 조회
        Group group = findByIdAndMemberId(id, command.memberId());

        // 2. 그룹명 변경
        group.rename(command.name());
        Group renamedGroup = groupRepository.rename(group);

        return GroupInfo.from(renamedGroup);
    }

    /**
     * 명함첩 그룹 순서 변경
     * 1. 그룹 ID 목록으로 그룹 목록 조회
     * 2. 본인 소유 그룹인지 검증
     * 3. 각 그룹의 순서 업데이트
     * 4. 저장
     * @param command 순서 변경 정보
     * @return 수정된 그룹 목록
     */
    @Transactional
    public List<GroupInfo> reorder(ReorderGroupCommand command) {
        String memberId = command.memberId();
        List<ReorderGroupCommand.GroupOrder> groupOrders = command.groups();

        // 1. 그룹 ID 목록으로 그룹 목록 조회
        List<String> groupIds = groupOrders.stream()
                .map(ReorderGroupCommand.GroupOrder::id)
                .toList();
        List<Group> groups = groupRepository.findAllByIds(groupIds);

        // 2. 본인 소유 그룹인지 검증
        boolean allOwnedByMember = groups.stream()
                .allMatch(group -> group.getMemberId().equals(memberId));
        if (!allOwnedByMember) {
            throw new CustomException(ErrorCode.GROUP_NOT_FOUND);
        }

        // 3. 각 그룹의 순서 업데이트
        Map<String, Integer> orderMap = groupOrders.stream()
                .collect(Collectors.toMap(
                        ReorderGroupCommand.GroupOrder::id,
                        ReorderGroupCommand.GroupOrder::sortOrder
                ));

        groups.forEach(group -> {
            Integer newOrder = orderMap.get(group.getId());
            if (newOrder != null) {
                group.reorder(newOrder);
            }
        });

        // 4. 저장
        List<Group> savedGroups = groupRepository.saveAll(groups);

        return savedGroups.stream()
                .map(GroupInfo::from)
                .toList();
    }

    /**
     * 그룹 아이디에 해당되는 그룹 삭제
     * @param id 그룹 아이디
     */
    @Transactional
    public void deleteGroup(String id) {
        groupRepository.deleteById(id);
    }
}

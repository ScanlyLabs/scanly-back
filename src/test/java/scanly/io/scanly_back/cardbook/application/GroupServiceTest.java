package scanly.io.scanly_back.cardbook.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import scanly.io.scanly_back.cardbook.application.dto.command.ReorderGroupCommand;
import scanly.io.scanly_back.cardbook.domain.Group;
import scanly.io.scanly_back.cardbook.domain.GroupRepository;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("GroupService 테스트")
class GroupServiceTest {

    @InjectMocks
    private GroupService groupService;

    @Mock
    private GroupRepository groupRepository;

    @Nested
    @DisplayName("reorder - 그룹 순서 변경 권한 검증")
    class Reorder {

        private final String memberId = "member-id";

        @Test
        @DisplayName("다른 사용자의 그룹이 포함되면 GROUP_NOT_FOUND 에러")
        void reorderFailWhenOtherMembersGroupIncluded() {
            // given
            Group myGroup = createGroup("group-1", memberId, "내 그룹", 0);
            String otherMemberId = "other-member-id";
            Group otherGroup = createGroup("group-2", otherMemberId, "남의 그룹", 1);
            List<Group> groups = List.of(myGroup, otherGroup);

            List<ReorderGroupCommand.GroupOrder> groupOrders = List.of(
                    new ReorderGroupCommand.GroupOrder("group-1", 1),
                    new ReorderGroupCommand.GroupOrder("group-2", 0)
            );
            ReorderGroupCommand command = new ReorderGroupCommand(memberId, groupOrders);

            given(groupRepository.findAllByIds(anyList())).willReturn(groups);

            // when & then
            assertThatThrownBy(() -> groupService.reorder(command))
                    .isInstanceOf(CustomException.class)
                    .satisfies(ex -> {
                        CustomException customEx = (CustomException) ex;
                        assertThat(customEx.getErrorCode()).isEqualTo(ErrorCode.GROUP_NOT_FOUND);
                    });

            verify(groupRepository, never()).reorder(anyList());
        }

        @Test
        @DisplayName("그룹의 순서 값이 올바르게 업데이트된다")
        void reorderUpdatesOrderCorrectly() {
            // given
            Group group1 = createGroup("group-1", memberId, "그룹1", 0);
            Group group2 = createGroup("group-2", memberId, "그룹2", 1);
            Group group3 = createGroup("group-3", memberId, "그룹3", 2);
            List<Group> groups = List.of(group1, group2, group3);

            List<ReorderGroupCommand.GroupOrder> groupOrders = List.of(
                    new ReorderGroupCommand.GroupOrder("group-1", 2),
                    new ReorderGroupCommand.GroupOrder("group-2", 0),
                    new ReorderGroupCommand.GroupOrder("group-3", 1)
            );
            ReorderGroupCommand command = new ReorderGroupCommand(memberId, groupOrders);

            given(groupRepository.findAllByIds(anyList())).willReturn(groups);
            given(groupRepository.reorder(anyList())).willAnswer(invocation -> {
                // service가 넘긴 groups를 인자를 꺼내서
                List<Group> updatedGroups = invocation.getArgument(0);
                // DB 저장했다고 치고 그대로 다시 return
                return updatedGroups;
            });

            // when
            groupService.reorder(command);

            // then
            assertThat(group1.getSortOrder()).isEqualTo(2);
            assertThat(group2.getSortOrder()).isEqualTo(0);
            assertThat(group3.getSortOrder()).isEqualTo(1);
        }
    }

    private Group createGroup(String id, String memberId, String name, int sortOrder) {
        return Group.of(
                id,
                memberId,
                name,
                sortOrder,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}

package scanly.io.scanly_back.cardbook.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Group 도메인 테스트")
class GroupTest {

    @Test
    @DisplayName("[Happy] 그룹명을 변경한다.")
    void rename() {
        // given
        Group group = createGroup();
        String newName = "신규 그룹명";

        // when
        group.rename(newName);

        // then
        assertThat(group.getName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("[Happy] 그룹 순서를 변경한다.")
    void reorder() {
        // given
        Group group = createGroup();
        int newOrder = 1;

        // when
        group.reorder(newOrder);

        // then
        assertThat(group.getSortOrder()).isEqualTo(newOrder);
    }

    private Group createGroup() {
        return Group.create(
                UUID.randomUUID().toString(),
                "그룹명",
                0
        );
    }
}
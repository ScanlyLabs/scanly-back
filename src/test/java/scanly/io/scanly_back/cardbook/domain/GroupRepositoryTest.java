package scanly.io.scanly_back.cardbook.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import scanly.io.scanly_back.IntegrationJpaTestSupport;
import scanly.io.scanly_back.cardbook.infrastructure.GroupJpaRepository;
import scanly.io.scanly_back.cardbook.infrastructure.GroupMapper;
import scanly.io.scanly_back.cardbook.infrastructure.GroupRepositoryImpl;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import({GroupRepositoryImpl.class, GroupMapper.class})
@DisplayName("GroupRepository 테스트")
class GroupRepositoryTest extends IntegrationJpaTestSupport {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupJpaRepository groupJpaRepository;

    @AfterEach
    void after() {
        groupJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("[Happy] 그룹이 존재하면 true를 리턴한다.")
    void existsByIdIsTrue() {
        // given
        Group group = createGroup();
        Group savedGroup =groupRepository.save(group);

        // when
        boolean result = groupRepository.existsById(savedGroup.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("[Happy] 그룹이 존재하지 않으면 false를 리턴한다.")
    void notExistsByIdIsFalse() {
        // given & when
        boolean result = groupRepository.existsById(UUID.randomUUID().toString());

        // then
        assertThat(result).isFalse();
    }

    private Group createGroup() {
        return Group.create(
                UUID.randomUUID().toString(),
                "그룹명",
                0
        );
    }
}
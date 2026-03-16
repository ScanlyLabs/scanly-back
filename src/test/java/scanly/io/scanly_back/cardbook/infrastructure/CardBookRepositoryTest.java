package scanly.io.scanly_back.cardbook.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import scanly.io.scanly_back.IntegrationJpaTestSupport;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.cardbook.domain.CardBook;
import scanly.io.scanly_back.cardbook.domain.CardBookRepository;
import scanly.io.scanly_back.cardbook.domain.model.ProfileSnapshot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Import({CardBookRepositoryImpl.class, CardBookMapper.class})
@DisplayName("CardBookRepository 테스트")
class CardBookRepositoryTest extends IntegrationJpaTestSupport {

    @Autowired
    private CardBookRepository cardBookRepository;

    @Test
    @DisplayName("명함첩을 저장한다.")
    void save() {
        // given
        ProfileSnapshot profileSnapshot = createProfileSnapshot();
        CardBook cardBook = createCardBookWithProfileSnapshot(profileSnapshot);

        // when
        CardBook savedCardBook = cardBookRepository.save(cardBook);

        // then
        assertThat(savedCardBook)
                .extracting("memberId", "cardId", "groupId", "memo", "isFavorite")
                .contains(
                        cardBook.getMemberId(),
                        cardBook.getCardId(),
                        cardBook.getGroupId(),
                        cardBook.getMemo(),
                        cardBook.isFavorite()
                );
        assertThat(cardBook.getProfileSnapshot())
                .extracting("name", "title", "company", "phone", "email")
                .contains(
                        profileSnapshot.name(),
                        profileSnapshot.title(),
                        profileSnapshot.company(),
                        profileSnapshot.phone(),
                        profileSnapshot.email()
                );
    }

    @Test
    @DisplayName("명함첩 목록을 저장한다.")
    void saveAll() {
        // given
        CardBook cardBook1 = createCardBook();
        CardBook cardBook2 = createCardBook();
        List<CardBook> cardBooks = List.of(cardBook1, cardBook2);

        // when
        List<CardBook> savedCardBook = cardBookRepository.saveAll(cardBooks);

        // then
        assertThat(savedCardBook)
                .extracting("memberId", "cardId")
                .containsExactlyInAnyOrder(
                        tuple(cardBook1.getMemberId(), cardBook1.getCardId()),
                        tuple(cardBook2.getMemberId(), cardBook2.getCardId())
                );
    }

    @Test
    @DisplayName("명함첩 아이디와 회원 아이디가 일치하는 명함첩이 존재할 경우 true를 반환한다.")
    void existsByIdAndMemberIdIsTrue() {
        // given
        CardBook cardBook = createCardBook();
        CardBook savedCardBook = cardBookRepository.save(cardBook);

        // when
        boolean exist = cardBookRepository.existsByIdAndMemberId(savedCardBook.getId(), savedCardBook.getMemberId());

        // then
        assertThat(exist).isTrue();
    }

    @Test
    @DisplayName("명함첩 아이디와 회원 아이디로 조회 시 명함첩 아이디가 일치하지 않을 경우 false를 반환한다.")
    void existsByIdAndMemberIdWhenIdIsDiffer() {
        // given
        CardBook cardBook = createCardBook();
        CardBook savedCardBook = cardBookRepository.save(cardBook);

        // when
        boolean exist = cardBookRepository.existsByIdAndMemberId("test", savedCardBook.getMemberId());

        // then
        assertThat(exist).isFalse();
    }

    @Test
    @DisplayName("명함첩 아이디와 회원 아이디로 조회 시 회원 아이디가 일치하지 않을 경우 false를 반환한다.")
    void existsByIdAndMemberIdWhenMemberIdIsDiffer() {
        // given
        CardBook cardBook = createCardBook();
        CardBook savedCardBook = cardBookRepository.save(cardBook);

        // when
        boolean exist = cardBookRepository.existsByIdAndMemberId(savedCardBook.getId(), "test");

        // then
        assertThat(exist).isFalse();
    }

    @Test
    @DisplayName("명함첩 아이디와 회원 아이디로 조회 시 명함첩 아이디와 회원 아이디가 일치하지 않을 경우 false를 반환한다.")
    void existsByIdAndMemberIdWhenIdAndMemberIdIsDiffer() {
        // given &&  when
        boolean exist = cardBookRepository.existsByIdAndMemberId("test", "test");

        // then
        assertThat(exist).isFalse();
    }

    @Test
    @DisplayName("회원 아이디와 명함 아이디로 조회 시 전부 일치하는 명함첩이 존재할 경우 true를 반환한다.")
    void existsByMemberIdAndCardIdIsTrue() {
        // given
        CardBook cardBook = createCardBook();
        CardBook savedCardBook = cardBookRepository.save(cardBook);

        // when
        boolean exist = cardBookRepository.existsByMemberIdAndCardId(savedCardBook.getMemberId(), savedCardBook.getCardId());

        // then
        assertThat(exist).isTrue();
    }

    @Test
    @DisplayName("회원 아이디와 명함 아이디로 조회 시 회원 아이디가 일치하지 않을 경우 false를 반환한다.")
    void existsByMemberIdAndCardIdWhenMemberIdIsDiffer() {
        // given
        CardBook cardBook = createCardBook();
        CardBook savedCardBook = cardBookRepository.save(cardBook);

        // when
        boolean exist = cardBookRepository.existsByMemberIdAndCardId("test", savedCardBook.getCardId());

        // then
        assertThat(exist).isFalse();
    }

    @Test
    @DisplayName("회원 아이디와 명함 아이디로 조회 시 명함 아이디가 일치하지 않을 경우 false를 반환한다.")
    void existsByMemberIdAndCardIdWhenCardIdIsDiffer() {
        // given
        CardBook cardBook = createCardBook();
        CardBook savedCardBook = cardBookRepository.save(cardBook);

        // when
        boolean exist = cardBookRepository.existsByMemberIdAndCardId(savedCardBook.getMemberId(), "test");

        // then
        assertThat(exist).isFalse();
    }

    @Test
    @DisplayName("회원 아이디와 명함 아이디로 조회 시 전부 일치하지 않을 경우 false를 반환한다.")
    void existsByMemberIdAndCardIdWhenMemberIdAndCardIdIsDiffer() {
        // given && when
        boolean exist = cardBookRepository.existsByMemberIdAndCardId("test", "test");

        // then
        assertThat(exist).isFalse();
    }

    @Test
    @DisplayName("전체 명함첩 목록을 조회한다.")
    void findAll() {
        // given
        cardBookRepository.saveAll(List.of(createCardBook(), createCardBook()));

        // when
        List<CardBook> cardBooks = cardBookRepository.findAll();

        // then
        assertThat(cardBooks).hasSize(2);
    }

    @Test
    @DisplayName("회원 아이디로 명함첩 목록을 조회한다.")
    void findAllByMemberId() {
        // given
        String memberId = UUID.randomUUID().toString();
        CardBook cardBook1 = createCardBookWithMemberId(memberId);
        CardBook cardBook2 = createCardBookWithMemberId(memberId);
        CardBook cardBook3 = createCardBookWithMemberId(memberId);
        cardBookRepository.save(cardBook1);
        cardBookRepository.save(cardBook2);
        cardBookRepository.save(cardBook3);

        // when
        List<CardBook> cardBooks = cardBookRepository.findAllByMemberId(memberId);

        // then
        assertThat(cardBooks).hasSize(3);
        assertThat(cardBooks)
                .allMatch(cardBook ->
                        cardBook.getMemberId().equals(memberId)
                );
    }

    @Test
    @DisplayName("회원 아이디가 일치하지 않을 경우 명함첩 목록을 비어있다.")
    void findAllByMemberIdWhenMemberIdIsDiffer() {
        // given && when
        List<CardBook> cardBooks = cardBookRepository.findAllByMemberId("test");

        // then
        assertThat(cardBooks).isEmpty();
    }

    @Test
    @DisplayName("회원 아이디로 명함첩 목록 조회 시 페이지 수만큼만 조회된다.")
    void findAllByMemberIdWithPageable() {
        // given
        String memberId = UUID.randomUUID().toString();
        List<CardBook> cardBooks = List.of(
                createCardBookWithMemberId(memberId),
                createCardBookWithMemberId(memberId),
                createCardBookWithMemberId(memberId)
        );
        cardBookRepository.saveAll(cardBooks);

        // when
        Pageable pageable = Pageable.ofSize(2);
        Page<CardBook> cardBookPage = cardBookRepository.findAllByMemberId(memberId, pageable);

        // then
        assertThat(cardBookPage).hasSize(2);
        assertThat(cardBookPage.getContent())
                .allMatch(cardBook ->
                        cardBook.getMemberId().equals(memberId)
                );
    }

    @Test
    @DisplayName("회원 아이디와 그룹 아이디로 명함첩 목록 조회 시 페이지 수만큼만 조회된다.")
    void findAllByMemberIdAndGroupId() {
        // given
        String memberId = UUID.randomUUID().toString();
        String groupId = UUID.randomUUID().toString();
        List<CardBook> cardBooks = List.of(
                createCardBookWithMemberIdAndGroupId(memberId, groupId),
                createCardBookWithMemberIdAndGroupId(memberId, groupId),
                createCardBookWithMemberIdAndGroupId(memberId, groupId)
        );
        cardBookRepository.saveAll(cardBooks);

        // when
        Pageable pageable = Pageable.ofSize(2);
        Page<CardBook> cardBookPage = cardBookRepository.findAllByMemberIdAndGroupId(memberId, groupId, pageable);

        // then
        assertThat(cardBookPage).hasSize(2);
        assertThat(cardBookPage.getContent())
                .allMatch(cardBook ->
                        cardBook.getMemberId().equals(memberId) &&
                                cardBook.getGroupId().equals(groupId));
    }

    @Test
    @DisplayName("명함첩 아이디와 회원 아이디로 명함첩을 조회한다.")
    void findByIdAndMemberId() {
        // given
        CardBook cardBook = createCardBook();
        CardBook savedCardBook = cardBookRepository.save(cardBook);

        // when
        Optional<CardBook> foundCardBook = cardBookRepository.findByIdAndMemberId(savedCardBook.getId(), savedCardBook.getMemberId());

        // then
        assertThat(foundCardBook).isPresent();
        assertThat(foundCardBook.get())
                .extracting("memberId", "cardId")
                .contains(
                        foundCardBook.get().getMemberId(), foundCardBook.get().getCardId()
                );
    }

    @Test
    @DisplayName("명함첩을 업데이트한다.")
    void update() {
        // given
        CardBook cardBook = createCardBook();
        String groupId = UUID.randomUUID().toString();
        cardBook.updateGroup(groupId);

        // when
        CardBook updatedCardBook = cardBookRepository.update(cardBook);

        // then
        assertThat(updatedCardBook.getGroupId()).isEqualTo(groupId);
    }

    @Test
    @DisplayName("명함첩 아이디로 명함첩을 삭제한다.")
    void deleteById() {
        // given
        String memberId = UUID.randomUUID().toString();
        CardBook cardBook1 = createCardBookWithMemberId(memberId);
        CardBook cardBook2 = createCardBookWithMemberId(memberId);
        List<CardBook> cardBooks = cardBookRepository.saveAll(List.of(cardBook1, cardBook2));

        // when
        String deletedCardBookId = cardBooks.get(0).getId();
        String cardBookId = cardBooks.get(1).getId();
        cardBookRepository.deleteById(deletedCardBookId);

        // then
        List<CardBook> foundCardBooks = cardBookRepository.findAllByMemberId(memberId);
        assertThat(foundCardBooks).hasSize(1);
        assertThat(foundCardBooks)
                .extracting("id")
                .containsExactly(cardBookId);
    }

    @Test
    @DisplayName("회원 아이디로 명함첩 개수를 조회한다.")
    void countByMemberId() {
        // given
        String memberId = UUID.randomUUID().toString();
        CardBook cardBook1 = createCardBookWithMemberId(memberId);
        CardBook cardBook2 = createCardBookWithMemberId("test");
        cardBookRepository.saveAll(List.of(cardBook1, cardBook2));

        // when
        long count = cardBookRepository.countByMemberId(memberId);

        // then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("회원 아이디가 일치하면서 즐겨찾기가 true인 개수를 조회한다.")
    void countByMemberIdAndFavorite() {
        // given
        String memberId = UUID.randomUUID().toString();
        CardBook cardBook1 = createCardBookWithMemberId(memberId);
        CardBook cardBook2 = createCardBookWithMemberId(memberId);
        CardBook cardBook3 = createCardBookWithMemberId("test");
        cardBook2.updateFavorite(true);
        cardBookRepository.saveAll(List.of(cardBook1, cardBook2, cardBook3));

        // when
        long count = cardBookRepository.countByMemberIdAndFavorite(memberId);

        // then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("회원 아이디와 최근 등록일로 명함첩 개수를 조회한다.")
    void countByMemberIdAndCreatedAtAfter() {
        // given
        String memberId = UUID.randomUUID().toString();
        CardBook cardBook1 = createCardBookWithMemberId(memberId);
        CardBook cardBook2 = createCardBookWithMemberId(memberId);
        cardBookRepository.saveAll(List.of(cardBook1, cardBook2));

        // when
        LocalDateTime now = LocalDateTime.now();
        long count = cardBookRepository.countByMemberIdAndCreatedAtAfter(memberId, now.minusDays(1));

        // then
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("회원 아이디와 최근 등록일로 조회 시 등록일이 미래일 경우 명함첩 개수는 0건이다.")
    void countByMemberIdAndCreatedAtAfterIsFalse() {
        // given
        String memberId = UUID.randomUUID().toString();
        CardBook cardBook1 = createCardBookWithMemberId(memberId);
        CardBook cardBook2 = createCardBookWithMemberId(memberId);
        cardBookRepository.saveAll(List.of(cardBook1, cardBook2));

        // when
        LocalDateTime now = LocalDateTime.now();
        long count = cardBookRepository.countByMemberIdAndCreatedAtAfter(memberId, now.plusDays(1));

        // then
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("회원아이디와 그룹아이디로 명함첩 개수를 조회한다.")
    void countByMemberIdAndGroupId() {
        // given
        String memberId = UUID.randomUUID().toString();
        String groupId = UUID.randomUUID().toString();
        CardBook cardBook1 = createCardBookWithMemberIdAndGroupId(memberId, groupId);
        CardBook cardBook2 = createCardBookWithMemberIdAndGroupId(memberId, groupId);
        CardBook cardBook3 = createCardBookWithMemberIdAndGroupId("test", groupId);
        CardBook cardBook4 = createCardBookWithMemberIdAndGroupId(memberId, "test");
        cardBookRepository.saveAll(List.of(cardBook1, cardBook2, cardBook3, cardBook4));

        // when
        long count = cardBookRepository.countByMemberIdAndGroupId(memberId, groupId);

        // then
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("명함 아이디가 일치하는 명함첩의 명함 아이디를 null로 변경한다.")
    void clearCardId() {
        // given
        String memberId = UUID.randomUUID().toString();
        String clearCardId = UUID.randomUUID().toString();
        String cardId = UUID.randomUUID().toString();
        CardBook cardBook1 = createCardBookWithCardId(clearCardId);
        CardBook cardBook2 = createCardBookWithCardId(cardId);
        CardBook cardBook3 = createCardBookWithCardId(cardId);
        cardBookRepository.saveAll(List.of(cardBook1, cardBook2, cardBook3));

        // when
        cardBookRepository.clearCardId(clearCardId);

        // then
        List<CardBook> cardBooks = cardBookRepository.findAll();
        assertThat(cardBooks).hasSize(3);
        assertThat(cardBooks)
                .extracting("cardId")
                .containsExactlyInAnyOrder(null, cardId, cardId);
    }

    @Test
    @DisplayName("회원아이디로 명함첩을 전부 삭제한다.")
    void deleteAllByMemberId() {
        // given
        String memberId = UUID.randomUUID().toString();
        CardBook cardBook1 = createCardBookWithMemberId(memberId);
        CardBook cardBook2 = createCardBookWithMemberId(memberId);
        CardBook cardBook3 = createCardBook();
        cardBookRepository.saveAll(List.of(cardBook1, cardBook2, cardBook3));

        // when
        cardBookRepository.deleteAllByMemberId(memberId);

        // then
        List<CardBook> cardBooks = cardBookRepository.findAll();
        assertThat(cardBooks).hasSize(1);
    }

    private CardBook createCardBook() {
        return CardBook.create(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                createProfileSnapshot(),
                null
        );
    }

    private CardBook createCardBookWithCardId(String cardId) {
        return CardBook.create(
                UUID.randomUUID().toString(),
                cardId,
                createProfileSnapshot(),
                null
        );
    }

    private CardBook createCardBookWithMemberIdAndGroupId(String memberId, String groupId) {
        return CardBook.create(
                memberId,
                UUID.randomUUID().toString(),
                createProfileSnapshot(),
                groupId
        );
    }

    private CardBook createCardBookWithMemberId(String memberId) {
        return CardBook.create(
                memberId,
                UUID.randomUUID().toString(),
                createProfileSnapshot(),
                null
        );
    }

    private CardBook createCardBookWithProfileSnapshot(ProfileSnapshot profileSnapshot) {
        return CardBook.create(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                profileSnapshot,
                null
        );
    }

    private ProfileSnapshot createProfileSnapshot() {
        return ProfileSnapshot.from(Card.create(
                UUID.randomUUID().toString(),
                "홍길동",
                "개발자",
                "스캔리",
                "01012341234",
                "test@example.com",
                null,
                null,
                null,
                null
        ));
    }
}

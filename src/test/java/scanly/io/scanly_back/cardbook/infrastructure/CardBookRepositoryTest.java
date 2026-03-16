package scanly.io.scanly_back.cardbook.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import scanly.io.scanly_back.IntegrationJpaTestSupport;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.cardbook.domain.CardBook;
import scanly.io.scanly_back.cardbook.domain.CardBookRepository;
import scanly.io.scanly_back.cardbook.domain.model.ProfileSnapshot;

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
    }

    private CardBook createCardBook() {
        return CardBook.create(
                UUID.randomUUID().toString(),
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

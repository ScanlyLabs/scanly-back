package scanly.io.scanly_back.cardbook.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import scanly.io.scanly_back.IntegrationTestSupport;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.card.domain.CardRepository;
import scanly.io.scanly_back.card.infrastructure.CardJpaRepository;
import scanly.io.scanly_back.cardbook.application.dto.command.SaveCardBookCommand;
import scanly.io.scanly_back.cardbook.application.dto.info.RegisterCardBookInfo;
import scanly.io.scanly_back.cardbook.domain.CardBook;
import scanly.io.scanly_back.cardbook.domain.CardBookRepository;
import scanly.io.scanly_back.cardbook.domain.model.ProfileSnapshot;
import scanly.io.scanly_back.cardbook.infrastructure.CardBookJpaRepository;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CardBookService 테스트")
class CardBookServiceTest extends IntegrationTestSupport {

    @Autowired
    private CardBookService cardBookService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardBookRepository cardBookRepository;

    @Autowired
    private CardJpaRepository cardJpaRepository;

    @Autowired
    private CardBookJpaRepository cardBookJpaRepository;

    @AfterEach
    void after() {
        cardJpaRepository.deleteAllInBatch();
        cardBookJpaRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("save - 명함 저장 검증")
    class Save {
        @Test
        @DisplayName("[Happy] 명함을 저장한다.")
        void save() {
            // given
            String memberId = UUID.randomUUID().toString();
            Card card = createCard();
            Card savedCard = cardRepository.save(card);

            SaveCardBookCommand command
                    = new SaveCardBookCommand(memberId, savedCard.getId(), null);

            // when
            RegisterCardBookInfo info = cardBookService.save(command);

            // then
            assertThat(info)
                    .extracting(
                            "cardId", "name", "title", "company", "groupId"
                    ).contains(
                            savedCard.getId(), savedCard.getName(), savedCard.getTitle(), savedCard.getCompany(), command.groupId()
                    );
            assertThat(info.profileSnapshot())
                    .extracting(
                            "name", "title", "company", "phone", "email",
                            "bio", "profileImageUrl", "portfolioUrl", "location"
                    ).contains(
                            savedCard.getName(), savedCard.getTitle(), savedCard.getCompany(), savedCard.getPhone(), savedCard.getEmail(),
                            savedCard.getBio(), savedCard.getProfileImageUrl(), savedCard.getPortfolioUrl(), savedCard.getLocation()
                    );
        }

        @Test
        @DisplayName("[Bad] 존재하지 않은 명함을 저장할 경우 실패한다.")
        void cardNotFound() {
            // given
            String memberId = UUID.randomUUID().toString();
            Card card = createCard();
            Card savedCard = cardRepository.save(card);

            SaveCardBookCommand command
                    = new SaveCardBookCommand(memberId, "card-123", null);

            // when & then
            assertThatThrownBy(() -> cardBookService.save(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CARD_NOT_FOUND);
        }

        @Test
        @DisplayName("[Bad] 자신의 명함을 저장하려고 하면 실패한다.")
        void ownCardSave() {
            // given
            String memberId = UUID.randomUUID().toString();
            Card card = createdCardWithMemberId(memberId);
            Card savedCard = cardRepository.save(card);

            SaveCardBookCommand command
                    = new SaveCardBookCommand(memberId, savedCard.getId(), null);

            // when & then
            assertThatThrownBy(() -> cardBookService.save(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CANNOT_SAVE_OWN_CARD);
        }

        @Test
        @DisplayName("[Bad] 명함첩을 중복으로 저장하려고 하면 실패한다.")
        void duplicateSave() {
            // given
            String memberId = UUID.randomUUID().toString();
            Card card = createCard();
            Card savedCard = cardRepository.save(card);
            CardBook cardBook = createCardBookWithMemberIdAndCard(memberId, savedCard);
            cardBookRepository.save(cardBook);

            SaveCardBookCommand command
                    = new SaveCardBookCommand(memberId, savedCard.getId(), null);

            // when & then
            assertThatThrownBy(() -> cardBookService.save(command))
                    .isInstanceOf(CustomException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CARD_BOOK_ALREADY_EXISTS);
        }
    }

    private CardBook createCardBookWithMemberIdAndCard(String memberId, Card card) {
        return CardBook.create(
                memberId,
                card.getId(),
                ProfileSnapshot.from(card),
                null
        );
    }

    private Card createdCardWithMemberId(String memberId) {
        return Card.create(
                memberId,
                "이름",
                "타이틀",
                "회사",
                "01012341234",
                "test@test.com",
                null,
                null,
                null,
                null
        );
    }

    private Card createCard() {
        return Card.create(
                UUID.randomUUID().toString(),
                "이름",
                "타이틀",
                "회사",
                "01012341234",
                "test@test.com",
                null,
                null,
                null,
                null
        );
    }
}

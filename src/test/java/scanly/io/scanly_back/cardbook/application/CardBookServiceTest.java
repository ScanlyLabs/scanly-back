package scanly.io.scanly_back.cardbook.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import scanly.io.scanly_back.card.application.CardService;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.cardbook.application.dto.command.SaveCardBookCommand;
import scanly.io.scanly_back.cardbook.application.dto.info.RegisterCardBookInfo;
import scanly.io.scanly_back.cardbook.domain.CardBook;
import scanly.io.scanly_back.cardbook.domain.CardBookRepository;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("CardBookService 테스트")
class CardBookServiceTest {

    @InjectMocks
    private CardBookService cardBookService;

    @Mock
    private CardBookRepository cardBookRepository;

    @Mock
    private CardService cardService;

    @Nested
    @DisplayName("save - 명함 저장 검증")
    class Save {

        private final String myMemberId = "my-member-id";
        private final String otherMemberId = "other-member-id";
        private final String cardId = "card-123";

        @Test
        @DisplayName("본인 명함 저장 시 CANNOT_SAVE_OWN_CARD 에러")
        void saveFailWhenSavingOwnCard() {
            // given
            Card myCard = createCard(cardId, myMemberId);
            SaveCardBookCommand command = new SaveCardBookCommand(myMemberId, cardId, null);

            given(cardService.findById(cardId)).willReturn(myCard);

            // when & then
            assertThatThrownBy(() -> cardBookService.save(command))
                    .isInstanceOf(CustomException.class)
                    .satisfies(ex -> {
                        CustomException customEx = (CustomException) ex;
                        assertThat(customEx.getErrorCode()).isEqualTo(ErrorCode.CANNOT_SAVE_OWN_CARD);
                    });

            verify(cardBookRepository, never()).save(any());
        }

        @Test
        @DisplayName("이미 저장된 명함을 다시 저장하면 CARD_BOOK_ALREADY_EXISTS 에러")
        void saveFailWhenCardAlreadyExists() {
            // given
            Card otherCard = createCard(cardId, otherMemberId);
            SaveCardBookCommand command = new SaveCardBookCommand(myMemberId, cardId, null);

            given(cardService.findById(cardId)).willReturn(otherCard);
            given(cardBookRepository.existsByMemberIdAndCardId(myMemberId, cardId)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> cardBookService.save(command))
                    .isInstanceOf(CustomException.class)
                    .satisfies(ex -> {
                        CustomException customEx = (CustomException) ex;
                        assertThat(customEx.getErrorCode()).isEqualTo(ErrorCode.CARD_BOOK_ALREADY_EXISTS);
                    });

            verify(cardBookRepository, never()).save(any());
        }

        @Test
        @DisplayName("타인의 새 명함 저장 시 성공")
        void saveSuccessWithOtherMembersNewCard() {
            // given
            Card otherCard = createCard(cardId, otherMemberId);
            SaveCardBookCommand command = new SaveCardBookCommand(myMemberId, cardId, null);
            CardBook savedCardBook = createCardBook(myMemberId, cardId);

            given(cardService.findById(cardId)).willReturn(otherCard);
            given(cardBookRepository.existsByMemberIdAndCardId(myMemberId, cardId)).willReturn(false);
            given(cardBookRepository.save(any(CardBook.class))).willReturn(savedCardBook);

            // when
            RegisterCardBookInfo result = cardBookService.save(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.cardId()).isEqualTo(cardId);
            verify(cardBookRepository).save(any(CardBook.class));
        }
    }

    private Card createCard(String cardId, String memberId) {
        return Card.of(
                cardId,
                memberId,
                "홍길동",
                "개발자",
                "Test",
                "01012345678",
                "test@email.com",
                "안녕하세요",
                List.of(),
                null,
                null,
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private CardBook createCardBook(String memberId, String cardId) {
        return CardBook.of(
                "cardbook-123",
                memberId,
                cardId,
                null,
                null,
                null,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}

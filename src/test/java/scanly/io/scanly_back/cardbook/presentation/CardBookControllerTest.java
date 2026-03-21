package scanly.io.scanly_back.cardbook.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import scanly.io.scanly_back.ControllerTestSupport;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.cardbook.application.CardBookService;
import scanly.io.scanly_back.cardbook.application.dto.info.RegisterCardBookInfo;
import scanly.io.scanly_back.cardbook.domain.CardBook;
import scanly.io.scanly_back.cardbook.domain.model.ProfileSnapshot;
import scanly.io.scanly_back.cardbook.presentation.dto.request.SaveCardBookRequest;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

@DisplayName("CardBookController 테스트")
class CardBookControllerTest extends ControllerTestSupport {

    @MockitoBean
    private CardBookService cardBookService;

    private static final String API_URL = "/api/cardbooks/v1";

    @Test
    @DisplayName("[Happy] 타인의 명함을 내 명함첩에 저장한다.")
    void saveCardBook() throws Exception {
        // given
        String memberId = UUID.randomUUID().toString();
        SaveCardBookRequest request
                = new SaveCardBookRequest(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        CardBook cardBook = createCardBookWithCard(createCard());
        RegisterCardBookInfo info = new RegisterCardBookInfo(
                cardBook.getId(),
                cardBook.getCardId(),
                cardBook.getProfileSnapshot().name(),
                cardBook.getProfileSnapshot().title(),
                cardBook.getProfileSnapshot().company(),
                cardBook.getProfileSnapshot().profileImageUrl(),
                cardBook.getProfileSnapshot(),
                cardBook.getGroupId(),
                cardBook.getMemo(),
                cardBook.isFavorite(),
                cardBook.getCreatedAt()
        );
        when(cardBookService.save(any())).thenReturn(info);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
                        .with(authentication(new UsernamePasswordAuthenticationToken(memberId, null, Collections.emptyList())))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("[Bad] 타인의 명함을 내 명함첩에 저장 시 cardId가 null이면 400 에러가 발생한다.")
    void saveCardBookCardIdNull() throws Exception {
        // given
        String memberId = UUID.randomUUID().toString();
        SaveCardBookRequest request = new SaveCardBookRequest(null, null);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
                        .with(authentication(new UsernamePasswordAuthenticationToken(memberId, null, Collections.emptyList())))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("cardId: 명함 ID는 필수입니다."));
    }

    private CardBook createCardBookWithCard(Card card) {
        return CardBook.create(
                UUID.randomUUID().toString(),
                card.getId(),
                ProfileSnapshot.from(card),
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
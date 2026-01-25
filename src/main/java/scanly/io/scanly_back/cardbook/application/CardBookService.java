package scanly.io.scanly_back.cardbook.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.card.application.CardService;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.cardbook.application.dto.command.CardExchangeCommand;
import scanly.io.scanly_back.cardbook.application.dto.command.SaveCardBookCommand;
import scanly.io.scanly_back.cardbook.application.dto.info.CardBookInfo;
import scanly.io.scanly_back.cardbook.application.dto.info.CardExchangeInfo;
import scanly.io.scanly_back.cardbook.domain.CardBook;
import scanly.io.scanly_back.cardbook.domain.CardBookRepository;
import scanly.io.scanly_back.cardbook.domain.CardExchange;
import scanly.io.scanly_back.cardbook.domain.CardExchangeRepository;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardBookService {

    private final CardBookRepository cardBookRepository;
    private final CardExchangeRepository cardExchangeRepository;
    private final CardService cardService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 명함 저장
     * 1. 명함 조회
     * 2. 유효성 검증
     * 3. 스냅샷 생성
     * 4. 명함첩에 저장
     * @param command 저장 정보
     * @return 저장된 명함첩 정보
     */
    @Transactional
    public CardBookInfo save(SaveCardBookCommand command) {
        String memberId = command.memberId();
        String cardId = command.cardId();

        // 1. 명함 조회
        Card card = cardService.findById(cardId);

        // 2. 유효성 검증
        validateCardBook(card.getMemberId(), memberId, cardId);

        // 4. 스냅샷 생성
        String profileSnapshot = createProfileSnapshot(card);

        // 5. 명함첩에 저장
        CardBook cardBook = CardBook.create(memberId, cardId, profileSnapshot, command.groupId());
        CardBook savedCardBook = cardBookRepository.save(cardBook);

        return CardBookInfo.from(savedCardBook);
    }

    /**
     * 명함 교환 내역 저장
     * 1. 명함 조회
     * 2. 명함 교환 내역 저장
     * 3. 알림 전송
     * @param command 명함 교환 정보
     * @return 저장된 명함교환 정보
     */
    @Transactional
    public CardExchangeInfo cardExchange(CardExchangeCommand command) {
        Card card = cardService.findById(command.cardId());

        // 2. 명함 교환 내역 저장
        CardExchange cardExchange = CardExchange.create(
                command.senderId(),
                card.getMemberId()
        );
        CardExchange savedCardExchange = cardExchangeRepository.save(cardExchange);

        // 3. 알림 전송

        return CardExchangeInfo.from(savedCardExchange);
    }

    /**
     * 명함첩 유효성 검사
     * - 타인 명함이 맞는지, 중복 저장은 아닌지 검사
     * @param cardMemberId 명함첩 소유자 아이디
     * @param memberId 회원 아이디
     * @param cardId 명함 아이디
     */
    private void validateCardBook(String cardMemberId, String memberId, String cardId) {
        // 보인 소유 명함 검증
        if (cardMemberId.equals(memberId)) {
            throw new CustomException(ErrorCode.CANNOT_SAVE_OWN_CARD);
        }

        // 중복 저장 검증
        if (cardBookRepository.existsByMemberIdAndCardId(memberId, cardId)) {
            throw new CustomException(ErrorCode.CARDBOOK_ALREADY_EXISTS);
        }
    }

    /**
     * 명함 정보를 JSON 스냅샷으로 변환
     * @param card 명함 정보
     * @return JSON 문자열
     */
    private String createProfileSnapshot(Card card) {
        try {
            Map<String, Object> snapshot = new HashMap<>();
            snapshot.put("name", card.getName());
            snapshot.put("title", card.getTitle());
            snapshot.put("company", card.getCompany());
            snapshot.put("phone", card.getPhone());
            snapshot.put("email", card.getEmail());
            snapshot.put("bio", card.getBio());
            snapshot.put("profileImageUrl", card.getProfileImageUrl());
            snapshot.put("portfolioUrl", card.getPortfolioUrl());
            snapshot.put("location", card.getLocation());
            return objectMapper.writeValueAsString(snapshot);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}

package scanly.io.scanly_back.cardbook.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
import scanly.io.scanly_back.cardbook.domain.event.CardExchangedEvent;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;
import scanly.io.scanly_back.member.domain.Member;
import scanly.io.scanly_back.member.domain.MemberRepository;
import scanly.io.scanly_back.notification.domain.model.NotificationTemplate;
import scanly.io.scanly_back.notification.domain.model.NotificationType;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardBookService {

    private final CardBookRepository cardBookRepository;
    private final CardExchangeRepository cardExchangeRepository;
    private final CardService cardService;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;
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
     * 3. 명함 교환 이벤트 발행
     * @param command 명함 교환 정보
     * @return 저장된 명함교환 정보
     */
    @Transactional
    public CardExchangeInfo cardExchange(CardExchangeCommand command) {
        Card card = cardService.findById(command.cardId());

        String senderId = command.senderId();
        String receiverId = card.getMemberId();

        // 2. 명함 교환 내역 저장
        CardExchange savedCardExchange = saveCardExchange(senderId, receiverId);

        // 3. 명함 교환 이벤트 발행
        publishCardExchangedEvent(senderId, receiverId);

        return CardExchangeInfo.from(savedCardExchange);
    }

    /**
     * 명함 교환 이벤트 발행
     * 1. 발신자 조회
     * 2. 명함 교환 이벤트 발행
     * @param senderId 발신자 아이디
     * @param receiverId 수신자 아이디
     * @throws JsonProcessingException JSON 변환 실패 예외
     */
    private void publishCardExchangedEvent(String senderId, String receiverId) {
        // 1. 발신자 조회
        Member member = memberRepository.findById(senderId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 2. 명함 교환 이벤트 발행
        String data;
        try {
            data = objectMapper.writeValueAsString(
                    Map.of("senderLoginId", member.getLoginId())
            );
        } catch (JsonProcessingException e) {
            log.error("Failed to publish CardExchangedEvent", e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        CardExchangedEvent cardExchangedEvent = CardExchangedEvent.of(
                receiverId,
                NotificationType.CARD_EXCHANGE,
                NotificationTemplate.CARD_EXCHANGE.getTitle(),
                NotificationTemplate.CARD_EXCHANGE.formatBody(member.getName()),
                data

        );
        eventPublisher.publishEvent(cardExchangedEvent);
    }

    /**
     * 명함 교환 내역 저장
     * @param senderId 발신자 아이디
     * @param receiverId 수신자 아이디
     * @return 저장된 명함 교환 내역
     */
    private CardExchange saveCardExchange(String senderId, String receiverId) {
        CardExchange cardExchange = CardExchange.create(
                senderId,
                receiverId
        );
        return cardExchangeRepository.save(cardExchange);
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

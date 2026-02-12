package scanly.io.scanly_back.cardbook.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import scanly.io.scanly_back.card.application.CardService;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.cardbook.application.dto.TagService;
import scanly.io.scanly_back.cardbook.application.dto.command.CardExchangeCommand;
import scanly.io.scanly_back.cardbook.application.dto.command.SaveCardBookCommand;
import scanly.io.scanly_back.cardbook.application.dto.command.UpdateCardBookFavoriteCommand;
import scanly.io.scanly_back.cardbook.application.dto.command.UpdateCardBookGroupCommand;
import scanly.io.scanly_back.cardbook.application.dto.command.UpdateCardBookMemoCommand;
import scanly.io.scanly_back.cardbook.application.dto.info.CardBookInfo;
import scanly.io.scanly_back.cardbook.application.dto.info.CardBookPreviewInfo;
import scanly.io.scanly_back.cardbook.application.dto.info.CardExchangeInfo;
import scanly.io.scanly_back.cardbook.application.dto.info.RegisterCardBookInfo;
import scanly.io.scanly_back.cardbook.domain.*;
import scanly.io.scanly_back.cardbook.domain.event.CardExchangedEvent;
import scanly.io.scanly_back.cardbook.domain.model.ProfileSnapshot;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;
import scanly.io.scanly_back.common.ratelimit.RateLimiterService;
import scanly.io.scanly_back.member.domain.Member;
import scanly.io.scanly_back.member.domain.MemberRepository;
import scanly.io.scanly_back.notification.domain.model.NotificationTemplate;
import scanly.io.scanly_back.notification.domain.model.NotificationType;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardBookService {

    private static final int DAILY_EXCHANGE_LIMIT = 3;

    private final CardBookRepository cardBookRepository;
    private final CardExchangeRepository cardExchangeRepository;
    private final TagService tagService;
    private final CardService cardService;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final RateLimiterService rateLimiterService;
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
    public RegisterCardBookInfo save(SaveCardBookCommand command) {
        String memberId = command.memberId();
        String cardId = command.cardId();

        // 1. 명함 조회
        Card card = cardService.findById(cardId);

        // 2. 유효성 검증
        validateCardBook(card.getMemberId(), memberId, cardId);

        // 3. 스냅샷 생성(명함 정보를 스냅샷으로 변환)
        ProfileSnapshot profileSnapshot = ProfileSnapshot.from(card);

        // 4. 명함첩에 저장
        CardBook cardBook = CardBook.create(memberId, cardId, profileSnapshot, command.groupId());
        CardBook savedCardBook = cardBookRepository.save(cardBook);

        return RegisterCardBookInfo.from(savedCardBook, card);
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
            throw new CustomException(ErrorCode.CARD_BOOK_ALREADY_EXISTS);
        }
    }

    /**
     * 명함 교환 내역 저장
     * 1. 명함 조회
     * 2. 일일 교환 제한 확인
     * 3. 명함 교환 내역 저장
     * 4. 명함 교환 이벤트 발행
     * @param command 명함 교환 정보
     * @return 저장된 명함교환 정보
     */
    @Transactional
    public CardExchangeInfo cardExchange(CardExchangeCommand command) {
        Card card = cardService.findById(command.cardId());

        String senderId = command.senderId();
        String receiverId = card.getMemberId();

        // 2. 일일 교환 제한 확인 (동일 수신자에게 하루 3회 제한)
        if (!rateLimiterService.isDailyExchangeAllowed(senderId, receiverId, DAILY_EXCHANGE_LIMIT)) {
            throw new CustomException(ErrorCode.DAILY_EXCHANGE_LIMIT_EXCEEDED);
        }

        // 3. 명함 교환 내역 저장
        CardExchange savedCardExchange = saveCardExchange(senderId, receiverId);

        // 4. 명함 교환 이벤트 발행
        publishCardExchangedEvent(senderId, receiverId);

        return CardExchangeInfo.from(savedCardExchange);
    }

    /**
     * 명함 교환 이벤트 발행
     * 1. 발신자 조회
     * 2. 명함 교환 이벤트 발행
     * @param senderId 발신자 아이디
     * @param receiverId 수신자 아이디
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
     * 명함첩 목록 조회
     * @param memberId 회원 아이디
     * @return 조회된 명함첩 목록
     */
    public List<CardBookPreviewInfo> readAll(String memberId) {
        List<CardBook> cardBooks = cardBookRepository.findAllByMemberId(memberId);

        return enrichWithCardInfo(cardBooks);
    }

    /**
     * 명함첩 목록 페이징 조회
     * @param memberId 회원 아이디
     * @param groupId 명함첩 그룹 아이디
     * @param pageable 페이징 정보
     * @return 페이징된 명함첩 목록
     */
    public Page<CardBookPreviewInfo> readAll(String memberId, String groupId, Pageable pageable) {
        Page<CardBook> cardBookPage;
        if (!StringUtils.hasText(groupId)) {
            cardBookPage = cardBookRepository.findAllByMemberId(memberId, pageable);
        } else {
            cardBookPage = cardBookRepository.findAllByMemberIdAndGroupId(memberId, groupId, pageable);
        }

        List<CardBook> cardBooks = cardBookPage.getContent();
        List<String> cardIds = cardBooks.stream().map(CardBook::getCardId).toList();
        Map<String, Card> cardMap = cardService.findAllByIds(cardIds).stream()
                .collect(java.util.stream.Collectors.toMap(Card::getId, card -> card));

        return cardBookPage.map(cardBook -> CardBookPreviewInfo.from(cardBook, cardMap.get(cardBook.getCardId())));
    }

    /**
     * 명함첩 목록에 Card 정보 추가
     * @param cardBooks 명함첩 목록
     * @return Card 정보가 포함된 명함첩 정보 목록
     */
    private List<CardBookPreviewInfo> enrichWithCardInfo(List<CardBook> cardBooks) {
        List<String> cardIds = cardBooks.stream().map(CardBook::getCardId).toList();
        Map<String, Card> cardMap = cardService.findAllByIds(cardIds).stream()
                .collect(java.util.stream.Collectors.toMap(Card::getId, card -> card));

        return cardBooks.stream()
                .map(cardBook -> CardBookPreviewInfo.from(cardBook, cardMap.get(cardBook.getCardId())))
                .toList();
    }

    /**
     * 명함첩 상세 조회
     * @param memberId 회원 아이디
     * @param id 명함첩 아이디
     * @return 조회된 명함첩
     */
    public CardBookInfo read(String memberId, String id) {
        CardBook cardBook = getByIdAndMemberId(id, memberId);
        Card card = findCardOrNull(cardBook.getCardId());
        List<Tag> tagList = tagService.getAllByCardBookId(cardBook.getId());
        return CardBookInfo.from(cardBook, card, tagList);
    }

    /**
     * 명함첩 그룹 수정
     * 1. 명함첩 조회
     * 2. 명함첩 수정
     * 3. Card 조회
     * 4. 태그 조회
     * @param command 명함첩 정보
     * @return 수정된 명함첩
     */
    public CardBookInfo updateGroup(UpdateCardBookGroupCommand command) {
        // 1. 명함첩 조회
        CardBook cardBook = getByIdAndMemberId(command.id(), command.memberId());

        // 2. 명함첩 수정
        cardBook.updateGroup(command.groupId());
        CardBook updatedCardBook = cardBookRepository.update(cardBook);

        // 3. Card 조회
        Card card = findCardOrNull(updatedCardBook.getCardId());

        // 4. 태그 조회
        List<Tag> tagList = tagService.getAllByCardBookId(cardBook.getId());

        return CardBookInfo.from(updatedCardBook, card, tagList);
    }

    /**
     * 명함첩 아이디 및 회원 아이디로 명함첩 조회
     * @param id 아이디
     * @param memberId 회원 아이디
     * @return 조회된 명함첩
     */
    private CardBook getByIdAndMemberId(String id, String memberId) {
        return cardBookRepository.findByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.CARD_BOOK_NOT_FOUND));
    }

    /**
     * Card 조회 (없으면 null 반환)
     * @param cardId 명함 아이디
     * @return 조회된 명함 또는 null
     */
    private Card findCardOrNull(String cardId) {
        return cardService.findByIdOrNull(cardId);
    }

    /**
     * 명함첩 메모 수정
     * 1. 명함첩 조회
     * 2. 명함첩 수정
     * 3. Card 조회
     * 4. 태그 조회
     * @param command 명함첩 정보
     * @return 수정된 명함첩
     */
    public CardBookInfo updateMemo(UpdateCardBookMemoCommand command) {
        // 1. 명함첩 조회
        CardBook cardBook = getByIdAndMemberId(command.id(), command.memberId());

        // 2. 명함첩 수정
        cardBook.updateMemo(command.memo());
        CardBook updatedCardBook = cardBookRepository.update(cardBook);

        // 3. Card 조회 후 반환
        Card card = findCardOrNull(updatedCardBook.getCardId());

        // 4. 태그 조회
        List<Tag> tagList = tagService.getAllByCardBookId(cardBook.getId());

        return CardBookInfo.from(updatedCardBook, card, tagList);
    }

    /**
     * 명함첩 즐겨찾기 수정
     * 1. 명함첩 조회
     * 2. 명함첩 수정
     * 3. Card 조회
     * 4. 태그 조회
     * @param command 명함첩 정보
     * @return 수정된 명함첩
     */
    public CardBookInfo updateFavorite(UpdateCardBookFavoriteCommand command) {
        // 1. 명함첩 조회
        CardBook cardBook = getByIdAndMemberId(command.id(), command.memberId());

        // 2. 명함첩 수정
        cardBook.updateFavorite(command.favorite());
        CardBook updatedCardBook = cardBookRepository.update(cardBook);

        // 3. Card 조회
        Card card = findCardOrNull(updatedCardBook.getCardId());

        // 4. 태그 조회
        List<Tag> tagList = tagService.getAllByCardBookId(cardBook.getId());

        return CardBookInfo.from(updatedCardBook, card, tagList);
    }

    /**
     * 명함첩 삭제
     * 1. 태그 삭제
     * 2. 명함첩 삭제
     * @param memberId 회원 아이디
     * @param id 아이디
     */
    @Transactional
    public void delete(String memberId, String id) {
        CardBook cardBook = getByIdAndMemberId(id, memberId);
        //1. 태그 삭제
        tagService.deleteAllByCardBookId(cardBook.getId());
        // 2. 명함첩 삭제
        cardBookRepository.deleteById(cardBook.getId());
    }

    /**
     * 명함첩 존재 여부 확인
     * @param memberId 회원 아이디
     * @param cardId 명함 아이디
     * @return 존재하면 true, 아니면 false
     */
    public boolean exists(String memberId, String cardId) {
        return cardBookRepository.existsByMemberIdAndCardId(memberId, cardId);
    }
}

package scanly.io.scanly_back.card.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.card.application.dto.info.ReadCardInfo;
import scanly.io.scanly_back.card.application.dto.info.RegisterCardInfo;
import scanly.io.scanly_back.card.application.dto.command.RegisterCardCommand;
import scanly.io.scanly_back.card.application.dto.command.SocialLinkCommand;
import scanly.io.scanly_back.card.application.dto.command.UpdateCardCommand;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.card.domain.CardRepository;
import scanly.io.scanly_back.cardbook.application.CardBookService;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;
import scanly.io.scanly_back.common.service.S3Service;
import scanly.io.scanly_back.common.util.QrCodeGenerator;
import scanly.io.scanly_back.member.application.MemberService;
import scanly.io.scanly_back.member.domain.Member;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final MemberService memberService;
    private final QrCodeGenerator qrCodeGenerator;
    private final S3Service s3Service;
    private final CardCacheService cardCacheService;
    private final CardBookService cardBookService;

    /**
     * 명함 등록
     * 1. 중복 명함 확인(인당 명함 1개)
     * 2. 회원 조회
     * 3. 명함 생성
     * 4. QR 코드 생성 및 S3 업로드
     * @param command 명함 등록 정보
     * @return 신규 명함 정보
     */
    @Transactional
    public RegisterCardInfo registerCard(RegisterCardCommand command) {
        String memberId = command.memberId();

        // 1. 중복 명함 확인(인당 명함 1개)
        validateDuplicateCard(memberId);

        // 2. 회원 조회
        Member member = memberService.findById(command.memberId());

        // 3. 명함 생성
        Card savedCard = registerCard(command, memberId, member.getName());

        // 4. QR 코드 생성 및 S3 업로드
        Card cardWithQr = generateQrCode(member.getLoginId(), savedCard);

        return RegisterCardInfo.from(cardWithQr);
    }

    /**
     * QR 코드 생성 및 S3 업로드
     * @param loginId 회원 로그인 아이디
     * @param card 수정할 명함
     * @return 수정된 명함
     */
    private Card generateQrCode(String loginId, Card card) {
        byte[] qrCodeBytes = qrCodeGenerator.generateQrCodeBytes(loginId);
        String qrCodeUrl = s3Service.uploadBytes(qrCodeBytes, "qrcodes", "png", "image/png");
        card.assignQrCode(qrCodeUrl);
        return cardRepository.updateOnlyCard(card);
    }

    /**
     * 명함 생성
     * @param command 명함 정보
     * @param memberId 회원 아이디
     * @param name 회원명
     * @return 저장된 명함
     */
    private Card registerCard(RegisterCardCommand command, String memberId, String name) {
        Card card = Card.create(
                memberId,
                name,
                command.title(),
                command.company(),
                command.phone(),
                command.email(),
                command.bio(),
                command.profileImageUrl(),
                command.portfolioUrl(),
                command.location()
        );
        // 소셜 링크 추가
        addSocialLinks(command.socialLinks(), card);
        return cardRepository.save(card);
    }

    /**
     * 명함 중복 확인
     * @param memberId 회원 아이디
     */
    private void validateDuplicateCard(String memberId) {
        if (cardRepository.existsByMemberId(memberId)) {
            throw new CustomException(ErrorCode.CARD_ALREADY_EXISTS);
        }
    }

    /**
     * 소셜 링크 추가
     * @param linkCommands 소셜 링크 커맨드 리스트
     * @param card 명함
     */
    private void addSocialLinks(List<SocialLinkCommand> linkCommands, Card card) {
        if (linkCommands != null) {
            for (SocialLinkCommand linkCommand : linkCommands) {
                card.addSocialLink(linkCommand.type(), linkCommand.url());
            }
        }
    }

    /**
     * 내 명함 조회
     * - 캐시 적용
     * @param memberId 회원 아이디
     * @return 조회된 명함
     */
    public ReadCardInfo readMyCard(String memberId) {
        return cardCacheService.getCardByMemberId(memberId);
    }

    /**
     * 로그인 ID로 명함 조회 (QR 스캔 시 사용)
     * - 캐시 적용
     * @param loginId 로그인 아이디
     * @return 조회된 명함
     */
    public ReadCardInfo readCardByLoginId(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        return cardCacheService.getCardByMemberId(member.getId());
    }

    /**
     * 명함 수정
     * - 캐시 무효화
     * @param command 수정할 명함 정보
     * @return 수정된 명함 정보
     */
    @Transactional
    public ReadCardInfo updateCard(UpdateCardCommand command) {
        Card card = findByMemberId(command.memberId());

        card.update(
                command.title(),
                command.company(),
                command.phone(),
                command.email(),
                command.bio(),
                command.profileImageUrl(),
                command.portfolioUrl(),
                command.location()
        );

        card.clearSocialLinks();
        addSocialLinks(command.socialLinks(), card);

        Card updatedCard = cardRepository.update(card);

        // 캐시 무효화
        cardCacheService.evictCache(command.memberId());

        return ReadCardInfo.from(updatedCard);
    }

    /**
     * 회원 아이디로 명함 조회
     * @param memberId 회원 아이디
     * @return 조회된 명함
     */
    private Card findByMemberId(String memberId) {
        return cardRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.CARD_NOT_FOUND));
    }

    /**
     * 아이디로 명함 조회
     * @param id 명함 아이디
     * @return 조회된 명함
     */
    public Card findById(String id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CARD_NOT_FOUND));
    }

    /**
     * 명함 ID 목록으로 명함 조회
     * @param ids 명함 ID 목록
     * @return 조회된 명함 목록
     */
    public List<Card> findAllByIds(List<String> ids) {
        return cardRepository.findAllByIds(ids);
    }

    /**
     * 내 명함 제거
     * 1. 명함 조회
     * 2. 명함첩 내 cardId null 로 변경 (참조 먼저 끊기)
     * 3. 명함 QR 이미지 제거
     * 4. 명함 제거
     * 5. 캐시 무효화
     * @param memberId 회원 아이디
     */
    @Transactional
    public void deleteMyCard(String memberId) {
        // 1. 명함 조회
        Card card = findByMemberId(memberId);
        // 2. 명함첩 내 cardId null 로 변경 (참조 먼저 끊기)
        cardBookService.clearCardId(card.getId());
        // 3. 명함 QR 이미지 제거
        s3Service.delete(card.getQrImageUrl());
        // 4. 명함 제거
        cardRepository.delete(card);
        // 5. 캐시 무효화
        cardCacheService.evictCache(memberId);
    }
}

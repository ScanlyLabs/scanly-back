package scanly.io.scanly_back.card.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.card.application.dto.info.ReadMeCardInfo;
import scanly.io.scanly_back.card.application.dto.info.RegisterCardInfo;
import scanly.io.scanly_back.card.application.dto.command.RegisterCardCommand;
import scanly.io.scanly_back.card.application.dto.command.SocialLinkCommand;
import scanly.io.scanly_back.card.application.dto.command.UpdateCardCommand;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.card.domain.CardRepository;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;

    /**
     * 명함 등록
     * 1. 중복 명함 확인(인당 명함 1개)
     * 2. 명함 생성
     * @param command 명함 등록 정보
     * @return 신규 명함 정보
     */
    @Transactional
    public RegisterCardInfo registerCard(RegisterCardCommand command) {
        String memberId = command.memberId();

        // 1. 중복 명함 확인(인당 명함 1개)
        validateDuplicateCard(memberId);

        // 2. 명함 생성
        Card card = Card.create(
                memberId,
                command.name(),
                command.title(),
                command.company(),
                command.phone(),
                command.email(),
                command.bio(),
                command.profileImageUrl(),
                command.portfolioUrl(),
                command.location()
        );

        addSocialLinks(command.socialLinks(), card);

        Card savedCard = cardRepository.save(card);

        return RegisterCardInfo.from(savedCard);
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
     * @param memberId 내 명함 아이디
     * @return 조회된 명함
     */
    public ReadMeCardInfo readMyCard(String memberId) {
        Card card = findByMemberId(memberId);

        return ReadMeCardInfo.from(card);
    }

    /**
     * 명함 수정
     * @param command 수정할 명함 정보
     * @return 수정된 명함 정보
     */
    @Transactional
    public ReadMeCardInfo updateCard(UpdateCardCommand command) {
        Card card = findByMemberId(command.memberId());

        card.update(
                command.name(),
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

        return ReadMeCardInfo.from(updatedCard);
    }

    /**
     * 회원 아이디로 명함 조회
     * @param memberId 회원 아이디
     * @return 조호된 명함
     */
    private Card findByMemberId(String memberId) {
        return cardRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.CARD_NOT_FOUND));
    }

    /**
     * 내 명함 제거
     * 1. 명함 조회
     * 2. 명함 제거
     * @param memberId 회원 아이디
     */
    @Transactional
    public void deleteMyCard(String memberId) {
        Card card = findByMemberId(memberId);
        cardRepository.delete(card);
    }
}

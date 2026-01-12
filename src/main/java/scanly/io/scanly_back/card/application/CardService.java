package scanly.io.scanly_back.card.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.card.application.dto.RegisterCardInfo;
import scanly.io.scanly_back.card.application.dto.RegisterCardCommand;
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

        addSocialLink(command, card);

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
     * @param command 등록할 명함 정보
     * @param card 등록할 명함
     */
    private static void addSocialLink(RegisterCardCommand command, Card card) {
        List<RegisterCardCommand.SocialLinkCommand> linkCommands = command.socialLinks();
        if (linkCommands != null) {
            for (RegisterCardCommand.SocialLinkCommand linkCommand : linkCommands) {
                card.addSocialLink(linkCommand.type(), linkCommand.url());
            }
        }
    }
}

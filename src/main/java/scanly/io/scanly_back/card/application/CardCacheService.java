package scanly.io.scanly_back.card.application;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import scanly.io.scanly_back.card.application.dto.info.ReadCardInfo;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.card.domain.CardRepository;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class CardCacheService {

    private final CardRepository cardRepository;

    /**
     * 회원 ID로 명함 조회 (캐시 적용)
     * @param memberId 회원 아이디
     * @return 조회된 명함 정보
     */
    @Cacheable(value = "card", key = "#memberId")
    public ReadCardInfo getCardByMemberId(String memberId) {
        Card card = cardRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.CARD_NOT_FOUND));
        return ReadCardInfo.from(card);
    }

    /**
     * 캐시 무효화
     * @param memberId 회원 아이디
     */
    @CacheEvict(value = "card", key = "#memberId")
    public void evictCache(String memberId) {
        // 캐시만 삭제
    }
}

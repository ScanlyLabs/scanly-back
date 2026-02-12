package scanly.io.scanly_back.cardbook.application.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.cardbook.application.dto.command.RegisterTagCommand;
import scanly.io.scanly_back.cardbook.application.dto.command.UpdateTagCommand;
import scanly.io.scanly_back.cardbook.application.dto.info.TagInfo;
import scanly.io.scanly_back.cardbook.domain.CardBookRepository;
import scanly.io.scanly_back.cardbook.domain.Tag;
import scanly.io.scanly_back.cardbook.infrastructure.TagRepository;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final CardBookRepository cardBookRepository;
    private final TagRepository tagRepository;

    /**
     * 아이디로 태그 조회
     * @return 조회된 태그
     */
    public Tag getById(String id) {
        return tagRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.TAG_NOT_FOUND));
    }

    /**
     * 태그 등록
     * 1. 자신이 소유한 명함첩인지 확인
     * 2. 10개 이상 등록되었는지 확인
     * 3. 태그 등록
     * @param command 태그 등록 정보
     * @return 신규 태그
     */
    @Transactional
    public TagInfo register(RegisterTagCommand command) {
        // 1. 자신이 소유한 명함첩인지 확인
        validateCardBookOwnership(command.cardBookId(), command.memberId());

        // 2. 10개 이상 등록되었는지 확인
        validateTagLimit(command.cardBookId());

        // 3. 태그 등록
        Tag tag = Tag.create(command.cardBookId(), command.name());
        Tag savedTag = tagRepository.save(tag);

        return TagInfo.from(savedTag);
    }

    /**
     * 태그가 10개 이상 등록되었는지 확인
     * @param cardBookId 명함첩 아이디
     */
    private void validateTagLimit(String cardBookId) {
        if (tagRepository.countByCardBookId(cardBookId) >= 10) {
            throw new CustomException(ErrorCode.TAG_LIMIT_EXCEEDED);
        }
    }

    /**
     * 명함첩 소유 검증
     * @param cardBookId 명함첩 아이디
     * @param memberId 회원 아이디
     */
    private void validateCardBookOwnership(String cardBookId, String memberId) {
        if (!cardBookRepository.existsByIdAndMemberId(cardBookId, memberId)) {
            throw new CustomException(ErrorCode.CARD_BOOK_NOT_FOUND);
        }
    }

    /**
     * 태그 수정
     * 1. 태그 조회
     * 2. 자신이 소유한 명함첩인지 확인
     * 3. 태그 수정
     * @param command 수정할 태그 정보
     * @return 수정된 태그
     */
    @Transactional
    public TagInfo update(UpdateTagCommand command) {
        // 1. 태그 조회
        Tag tag = getById(command.id());

        // 2. 자신이 소유한 명함첩인지 확인
        validateCardBookOwnership(tag.getCardBookId(), command.memberId());

        // 3. 태그 수정
        tag.changeName(command.name());
        Tag updatedTag = tagRepository.update(tag);

        return TagInfo.from(updatedTag);
    }

    /**
     * 태그 삭제
     * @param memberId 회원 아이디
     * @param id 아이디
     */
    @Transactional
    public void delete(String memberId, String id) {
        // 1. 태그 조회
        Tag tag = getById(id);

        // 2. 자신이 소유한 명함첩인지 확인
        validateCardBookOwnership(tag.getCardBookId(), memberId);

        // 3. 삭제
        tagRepository.deleteById(tag.getId());
    }

    /**
     * 명함첩 아이디로 태그 삭제
     * 상위 트랜잭션에 참여하기 때문에 @Transactional 생
     * @param cardBookId 명함첩 아이
     */
    public void deleteAllByCardBookId(String cardBookId) {
        tagRepository.deleteAllByCardBookId(cardBookId);
    }
}

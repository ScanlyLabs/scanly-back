package scanly.io.scanly_back.cardbook.application.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.cardbook.application.dto.command.RegisterTagCommand;
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
     * 태그 등록
     * @param command 태그 등록 정보
     * @return 신규 태그
     */
    @Transactional
    public TagInfo register(RegisterTagCommand command) {
        if (!cardBookRepository.existsByIdAndMemberId(command.cardBookId(), command.memberId())) {
            throw new CustomException(ErrorCode.CARD_BOOK_NOT_FOUND);
        }

        Tag tag = Tag.create(command.cardBookId(), command.name());
        Tag savedTag = tagRepository.save(tag);

        return TagInfo.from(savedTag);
    }
}

package scanly.io.scanly_back.cardbook.domain;

import scanly.io.scanly_back.cardbook.domain.model.ProfileSnapshot;
import scanly.io.scanly_back.common.domain.BaseDomain;

import java.time.LocalDateTime;

public class CardBook extends BaseDomain {
    private String id;
    private String memberId;             // 소유자 ID
    private String cardId;              // 원본 명함 ID
    private ProfileSnapshot profileSnapshot;      // 저장 당시 명함 정보
    private String groupId;             // 저장된 그룹 ID
    private String memo;                // 메모
    private boolean isFavorite;         // 즐겨찾기 여부

    private CardBook(
            String id, String memberId, String cardId,
            ProfileSnapshot profileSnapshot, String groupId, String memo,
            boolean isFavorite,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        super(createdAt, updatedAt);
        this.id = id;
        this.memberId = memberId;
        this.cardId = cardId;
        this.profileSnapshot = profileSnapshot;
        this.groupId = groupId;
        this.memo = memo;
        this.isFavorite = isFavorite;
    }

    public static CardBook of(
            String id, String memberId, String cardId,
            ProfileSnapshot profileSnapshot, String groupId,
            String memo, boolean isFavorite,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new CardBook(
                id, memberId, cardId,
                profileSnapshot, groupId,
                memo, isFavorite,
                createdAt, updatedAt
        );
    }

    public static CardBook create(String memberId, String cardId, ProfileSnapshot profileSnapshot, String groupId) {
        return new CardBook(
                null,
                memberId,
                cardId,
                profileSnapshot,
                groupId,
                null,
                false,
                null,
                null
        );
    }

    /**
     * 명함첩 그룹 수정
     * @param groupId 그룹 아이디
     */
    public void updateGroup(String groupId) {
        this.groupId = groupId;
    }

    /**
     * 명함첩 메모 수정
     * @param memo 메모
     */
    public void updateMemo(String memo) {
        this.memo = memo;
    }

    /**
     * 명함첩 즐겨찾기 수정
     * @param favorite 즐겨찾기 여부
     */
    public void updateFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    // getters
    public String getId() {
        return id;
    }
    public String getMemberId() {
        return memberId;
    }
    public String getCardId() {
        return cardId;
    }
    public ProfileSnapshot getProfileSnapshot() {
        return profileSnapshot;
    }
    public String getGroupId() {
        return groupId;
    }
    public String getMemo() {
        return memo;
    }
    public boolean isFavorite() {
        return isFavorite;
    }
}

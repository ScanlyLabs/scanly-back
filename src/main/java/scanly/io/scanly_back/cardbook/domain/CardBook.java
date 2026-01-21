package scanly.io.scanly_back.cardbook.domain;

import scanly.io.scanly_back.common.domain.BaseDomain;

public class CardBook extends BaseDomain {
    private String id;
    private String memberId;             // 소유자 ID
    private String cardId;              // 원본 명함 ID
    private String profileSnapshot;      // 저장 당시 명함 정보
    private String groupId;             // 저장된 그룹 ID
    private String memo;                // 메모
    private boolean isFavorite;         // 즐겨찾기 여부

    private CardBook(
            String id, String memberId, String cardId,
            String profileSnapshot, String groupId, String memo,
            boolean isFavorite
    ) {
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
            String profileSnapshot, String groupId,
            String memo, boolean isFavorite
    ) {
        return new CardBook(
                id, memberId, cardId,
                profileSnapshot, groupId,
                memo, isFavorite
        );
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
    public String getProfileSnapshot() {
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

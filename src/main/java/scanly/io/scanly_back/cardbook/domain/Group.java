package scanly.io.scanly_back.cardbook.domain;

import java.time.LocalDateTime;

public class Group {
    private String id;
    private String memberId;            // 소유자 ID
    private String name;                // 그룹명
    private int sortOrder;                  // 순서
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Group(
            String id, String memberId, String name, int sortOrder,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.sortOrder = sortOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Group of(
            String id, String memberId, String name, int sortOrder,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new Group(id, memberId, name, sortOrder, createdAt, updatedAt);
    }

    /**
     * 그룹 생성
     * @param memberId 회원 아이디
     * @param name 그룹명
     * @param sortOrder 순서
     * @return 그룹
     */
    public static Group create(String memberId, String name, int sortOrder) {
        LocalDateTime now = LocalDateTime.now();

        return new Group(
                null,
                memberId,
                name,
                sortOrder,
                now,
                now
        );
    }

    /**
     * 그룹명 변경
     * @param name 변경할 그룹명
     */
    public void rename(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 그룹 순서 변경
     * @param sortOrder 변경할 순서
     */
    public void reorder(int sortOrder) {
        this.sortOrder = sortOrder;
        this.updatedAt = LocalDateTime.now();
    }

    // getters
    public String getId() {
        return id;
    }
    public String getMemberId() {
        return memberId;
    }
    public String getName() {
        return name;
    }
    public int getSortOrder() {
        return sortOrder;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

package scanly.io.scanly_back.cardbook.domain;

import java.time.LocalDateTime;

public class Group {
    private String id;
    private String memberId;            // 소유자 ID
    private String name;                // 그룹명
    private int order;                  // 순서
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Group(
            String id, String memberId, String name, int order,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.order = order;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Group of(
            String id, String memberId, String name, int order,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new Group(id, memberId, name, order, createdAt, updatedAt);
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
    public int getOrder() {
        return order;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

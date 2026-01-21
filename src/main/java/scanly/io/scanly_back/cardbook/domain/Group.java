package scanly.io.scanly_back.cardbook.domain;

import scanly.io.scanly_back.common.domain.BaseDomain;

import java.time.LocalDateTime;

public class Group extends BaseDomain {

    private String id;
    private String memberId;
    private String name;
    private int sortOrder;

    private Group(
            String id, String memberId, String name, int sortOrder,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        super(createdAt, updatedAt);
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.sortOrder = sortOrder;
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
        return new Group(null, memberId, name, sortOrder, null, null);
    }

    /**
     * 그룹명 변경
     * @param name 변경할 그룹명
     */
    public void rename(String name) {
        this.name = name;
    }

    /**
     * 그룹 순서 변경
     * @param sortOrder 변경할 순서
     */
    public void reorder(int sortOrder) {
        this.sortOrder = sortOrder;
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
}

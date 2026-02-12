package scanly.io.scanly_back.cardbook.domain;

import scanly.io.scanly_back.common.domain.BaseDomain;

import java.time.LocalDateTime;

public class Tag extends BaseDomain {
    private String id;
    private String cardBookId;             // 명함첩 ID
    private String name;                // 태그명

    private Tag(
            String id, String cardBookId, String name,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        super(createdAt, updatedAt);
        this.id = id;
        this.cardBookId = cardBookId;
        this.name = name;
    }

    public static Tag of(
            String id, String cardBookId, String name,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new Tag(id, cardBookId, name, createdAt, updatedAt);
    }


    // getters
    public String getId() {
        return id;
    }

    public String getCardBookId() {
        return cardBookId;
    }

    public String getName() {
        return name;
    }
}

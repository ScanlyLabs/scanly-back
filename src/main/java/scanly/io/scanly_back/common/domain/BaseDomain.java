package scanly.io.scanly_back.common.domain;

import java.time.LocalDateTime;

public abstract class BaseDomain {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected BaseDomain() {
    }

    protected BaseDomain(LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

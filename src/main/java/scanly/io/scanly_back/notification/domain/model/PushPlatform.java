package scanly.io.scanly_back.notification.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PushPlatform {
    ANDROID,
    IOS;

    @JsonCreator
    public static PushPlatform from(String value) {
        return PushPlatform.valueOf(value.toUpperCase());
    }
}

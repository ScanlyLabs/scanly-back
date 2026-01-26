package scanly.io.scanly_back.notification.domain;

public interface PushTokenRepository {
    PushToken save(PushToken pushToken);
}

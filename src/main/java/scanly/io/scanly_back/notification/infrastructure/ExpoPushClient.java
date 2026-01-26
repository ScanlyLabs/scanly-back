package scanly.io.scanly_back.notification.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ExpoPushClient {

    private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

    private final WebClient webClient;

    public ExpoPushClient() {
        this.webClient = WebClient.builder()
                .baseUrl(EXPO_PUSH_URL)
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<Boolean> sendPushNotification(String pushToken, String title, String body, String data) {
        ExpoPushMessage message = new ExpoPushMessage(pushToken, title, body, data);

        return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(message)
                .retrieve()
                .bodyToMono(ExpoPushResponse.class)
                .map(response -> "ok".equals(response.data().status()))
                .onErrorReturn(false);
    }

    private record ExpoPushMessage(
            String to,
            String title,
            String body,
            String data
    ) {}

    private record ExpoPushResponse(
            @JsonProperty("data") ExpoPushData data
    ) {}

    private record ExpoPushData(
            String status,
            String id,
            String message
    ) {}
}

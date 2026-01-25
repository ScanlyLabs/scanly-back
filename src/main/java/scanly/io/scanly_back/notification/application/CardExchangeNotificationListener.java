package scanly.io.scanly_back.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import scanly.io.scanly_back.cardbook.domain.event.CardExchangedEvent;

@Component
@RequiredArgsConstructor
public class CardExchangeNotificationListener {

    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(CardExchangedEvent cardExchangedEvent) {
        notificationService.send(
                cardExchangedEvent.receiverId(),
                cardExchangedEvent.notificationType(),
                cardExchangedEvent.title(),
                cardExchangedEvent.body(),
                cardExchangedEvent.data()
        );
    }
}

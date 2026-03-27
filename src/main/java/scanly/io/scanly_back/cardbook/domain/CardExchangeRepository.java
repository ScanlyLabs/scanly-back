package scanly.io.scanly_back.cardbook.domain;

import java.util.Optional;

public interface CardExchangeRepository {
    CardExchange save(CardExchange cardExchange);
    Optional<CardExchange> findByIdAndReceiverId(String id, String receiverId);
    CardExchange updateStatus(CardExchange cardExchange);
}

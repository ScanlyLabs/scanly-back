package scanly.io.scanly_back.cardbook.application.dto.command;

public record AcceptExchangeCommand(
        String exchangeId,    // 교환 요청 ID
        String receiverId     // 수신자 (수락하는 사람)
) {
}

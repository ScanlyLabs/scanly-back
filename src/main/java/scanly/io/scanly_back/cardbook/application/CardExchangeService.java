package scanly.io.scanly_back.cardbook.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.cardbook.domain.CardExchangeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardExchangeService {

    private final CardExchangeRepository cardExchangeRepository;

    
}

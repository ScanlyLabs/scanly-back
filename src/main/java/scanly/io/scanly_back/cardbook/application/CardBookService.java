package scanly.io.scanly_back.cardbook.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.cardbook.domain.CardBookRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardBookService {

    private CardBookRepository cardBookRepository;
    
}

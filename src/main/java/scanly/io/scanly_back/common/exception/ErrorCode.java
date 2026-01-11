package scanly.io.scanly_back.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 커스텀 에러 코드
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

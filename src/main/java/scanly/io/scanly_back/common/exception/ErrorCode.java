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

    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "M003", "이미 사용 중인 로그인 아이디입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

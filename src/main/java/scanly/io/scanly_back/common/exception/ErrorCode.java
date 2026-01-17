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
    // auth
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "AT001", "비밀번호가 일치하지 않습니다"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AT006", "유효하지 않은 리프레시 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AT007", "리프레시 토큰을 찾을 수 없습니다."),

    // member
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "M001", "이미 사용 중인 로그인 아이디입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M002", "회원을 찾을 수 없습니다."),

    // card
    CARD_ALREADY_EXISTS(HttpStatus.CONFLICT, "CD001", "명함이 이미 존재합니다."),
    CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "CD002", "명함을 찾을 수 없습니다."),

    // common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "입력값이 올바르지 않습니다."),

    // server
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SE001", "에상치 못한 에러가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

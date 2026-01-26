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

    // card_book
    CARD_BOOK_ALREADY_EXISTS(HttpStatus.CONFLICT, "CB001", "이미 저장된 명함입니다."),
    CANNOT_SAVE_OWN_CARD(HttpStatus.BAD_REQUEST, "CB002", "본인 명함은 저장할 수 없습니다."),
    CARD_BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "CB003", "명함첩을 찾을 수 없습니다."),

    // group
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "CBG001", "명함첩 그룹이 존재하지 않습니다."),

    // push_token
    PUSH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "푸시 토큰이 존재하지 않습니다."),

    // common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "입력값이 올바르지 않습니다."),

    // server
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SE001", "에상치 못한 에러가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

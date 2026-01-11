package scanly.io.scanly_back.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import scanly.io.scanly_back.common.exception.ErrorCode;

/**
 * 공통 응답 객체
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ErrorResponse error;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, null, null);
    }

    public static ApiResponse<Void> error(ErrorCode errorCode) {
        return new ApiResponse<>(false, null, ErrorResponse.of(errorCode));
    }

    public static ApiResponse<Void> error(ErrorCode errorCode, String message) {
        return new ApiResponse<>(false, null, ErrorResponse.of(errorCode, message));
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ErrorResponse {
        private final String code;
        private final String message;

        public static ErrorResponse of(ErrorCode errorCode) {
            return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
        }

        public static ErrorResponse of(ErrorCode errorCode, String message) {
            return new ErrorResponse(errorCode.getCode(), message);
        }
    }
}

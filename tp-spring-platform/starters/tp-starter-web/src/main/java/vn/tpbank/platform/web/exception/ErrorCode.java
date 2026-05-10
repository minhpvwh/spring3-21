package vn.tpbank.platform.web.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INTERNAL_ERROR("ERR_INTERNAL", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("ERR_BAD_REQUEST", "Bad request", HttpStatus.BAD_REQUEST),
    VALIDATION_FAILED("203", "Validation failed", HttpStatus.BAD_REQUEST),
    NOT_FOUND("ERR_NOT_FOUND", "Resource not found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("ERR_UNAUTHORIZED", "Unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("ERR_FORBIDDEN", "Access denied", HttpStatus.FORBIDDEN),
    CONFLICT("ERR_CONFLICT", "Resource conflict", HttpStatus.CONFLICT),
    METHOD_NOT_ALLOWED("ERR_METHOD_NOT_ALLOWED", "Method not allowed", HttpStatus.METHOD_NOT_ALLOWED),
    SERVICE_UNAVAILABLE("ERR_SERVICE_UNAVAILABLE", "Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String defaultMessage, HttpStatus httpStatus) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

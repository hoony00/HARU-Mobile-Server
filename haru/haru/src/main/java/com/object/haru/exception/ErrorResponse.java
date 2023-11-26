package com.object.haru.exception;

import java.time.LocalDateTime;
import lombok.Getter;

/**
 *    JSON 에러 상황 응답
 *
 *   @version          1.00    2023.02.05
 *   @author           한승완
 */

@Getter
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus().value();
        this.error = errorCode.getStatus().name();
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
    }

}

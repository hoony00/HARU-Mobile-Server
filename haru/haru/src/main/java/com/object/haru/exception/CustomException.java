package com.object.haru.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *    커스텀으로 사용할 에러 핸들링 저장 파일
 *
 *   @version          1.00    2023.02.05
 *   @author           한승완
 */

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;


}
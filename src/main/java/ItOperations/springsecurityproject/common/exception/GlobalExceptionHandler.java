package ItOperations.springsecurityproject.common.exception;

import ItOperations.springsecurityproject.common.ApiUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static ItOperations.springsecurityproject.common.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static ItOperations.springsecurityproject.common.exception.ErrorCode.UNAUTHORIZED_MEMBER;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // custom 예외 핸들링
    @ExceptionHandler({ CustomException.class })
    protected ResponseEntity handleCustomException(CustomException ex) {
        return new ResponseEntity<>(ApiUtils.fail(ex.getErrorCode().getStatus(), ex.getErrorCode().getMessage()), HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    // 서버 내부 오류 500
    @ExceptionHandler({ Exception.class })
    protected ResponseEntity handleServerException(Exception ex) {
        return new ResponseEntity<>(ApiUtils.fail(INTERNAL_SERVER_ERROR.getStatus(), INTERNAL_SERVER_ERROR.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 인증 작업 중 사용자의 자격 증명 실패 401
    @ExceptionHandler({ BadCredentialsException.class })
    protected ResponseEntity badCredentialsException(Exception ex) {
        return new ResponseEntity<>(ApiUtils.fail(UNAUTHORIZED_MEMBER.getStatus(), UNAUTHORIZED_MEMBER.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}

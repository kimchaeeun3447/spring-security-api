package ItOperations.springsecurityproject.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 공통
    BAD_REQUEST(400, "잘못된 요청입니다."),
    NOT_FOUND(404,  "존재하지 않습니다."),
    INTERNAL_SERVER_ERROR(500, "서버 내부 에러입니다."),

    // Security, JWT
    UNAUTHORIZED_MEMBER(401, "인증되지 않은 사용자입니다."),
    FORBIDDEN_MEMBER(403, "권한이 없는 사용자입니다."),
    INVALID_REFRESH_TOKEN(403, "토큰이 유효하지 않습니다. 재로그인이 필요합니다."),
    NOT_FOUND_TOKEN(404, "토큰이 존재하지 않습니다. 재로그인이 필요합니다."),

    // Member
    INPUT_INVALID(400, "입력한 정보를 다시 확인해주세요."),
    NOT_FOUND_MEMBER(404,  "존재하지 않는 사용자입니다."),
    CONFLICT(409, "이미 등록된 계정입니다.");


    private final int status;
    private final String message;
}


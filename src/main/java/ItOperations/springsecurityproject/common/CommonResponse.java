package ItOperations.springsecurityproject.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/* 공통 Response 형식 - 과제 예시 기준 */
@Getter
public class CommonResponse<T> {

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;     // 데이터

    private int rtnCode;  // 상태 코드

    private String rtnMsg; // 응답 메시지


    // 성공 response
    @Builder
    public CommonResponse(T result, int code, String message) {
        this.result = result;
        this.rtnCode = code;
        this.rtnMsg = message;
    }

    // 에러 response
    @Builder
    public CommonResponse(int code, String message) {
        this.rtnCode = code;
        this.rtnMsg = message;
    }
}
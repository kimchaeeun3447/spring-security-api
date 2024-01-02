package ItOperations.springsecurityproject.common;

// CommonResponse 응답형식을 Controller에서 호출하기 위해
public class ApiUtils {
    // 성공 response
    public static <T> CommonResponse<T> success(T result, int code, String message) {
        return new CommonResponse<>(result, code, message);
    }

    // 에러 response
    public static <T> CommonResponse<T> fail(int code, String message) {
        return new CommonResponse<>(code, message);
    }
}
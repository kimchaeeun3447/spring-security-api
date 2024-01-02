package ItOperations.springsecurityproject.common;

// CommonResponse 응답형식을 Controller에서 호출하기 위해
public class ApiUtils {
    public static <T> CommonResponse<T> success(T result, int code, String message) {
        return new CommonResponse<>(result, code, message);
    }
}
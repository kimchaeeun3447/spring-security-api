package ItOperations.springsecurityproject.member.dto;

import lombok.Getter;
import lombok.Setter;

/* 로그인 Request DTO
*
* - 요청 예시 :
* {
    "body": {"id": "아이디", "pw": "비밀번호" },
    "token": "토큰"
   }
*/
@Getter
public class LoginRequest {

    private Body body;

    private String token;

    @Getter
    public static class Body {
        private String id;
        private String pw;
    }

}

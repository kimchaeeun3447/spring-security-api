package ItOperations.springsecurityproject.member.dto;

import ItOperations.springsecurityproject.member.domain.Authority;
import ItOperations.springsecurityproject.member.domain.Member;
import ItOperations.springsecurityproject.security.token.TokenDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/* 회원 정보 Response DTO */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignResponse {
    private Long id;

    private String accountId;

    private String email;

    private List<Authority> roles = new ArrayList<>();

    // access, refresh 토큰
    private TokenDto token;

    public SignResponse(Member member) {
        this.id = member.getId();
        this.accountId = member.getAccountId();
        this.email = member.getEmail();
        this.roles = member.getRoles();
    }
}
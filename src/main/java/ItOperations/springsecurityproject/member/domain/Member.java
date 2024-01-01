package ItOperations.springsecurityproject.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String accountId;       // 계정 아이디

    private String password;        // 계정 비밀번호

    @Column(unique = true)
    private String email;           // 이메일

    private String refreshToken;    // Refresh 토큰
}

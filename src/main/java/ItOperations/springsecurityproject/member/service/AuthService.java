package ItOperations.springsecurityproject.member.service;

import ItOperations.springsecurityproject.member.domain.Authority;
import ItOperations.springsecurityproject.member.domain.Member;
import ItOperations.springsecurityproject.member.dto.LoginRequest;
import ItOperations.springsecurityproject.member.dto.SignRequest;
import ItOperations.springsecurityproject.member.dto.SignResponse;
import ItOperations.springsecurityproject.member.repository.MemberRepository;
import ItOperations.springsecurityproject.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // 로그인 인증
    public SignResponse login(LoginRequest request) throws Exception {
        // 계정 아이디로 Member 조회
        System.out.println("body값은 = " + request);
        System.out.println("id값은 = " + request.getBody().getId());
        Member member = memberRepository.findByAccountId(request.getBody().getId()).orElseThrow(() ->
                new BadCredentialsException("잘못된 아이디입니다."));
        System.out.println("member는 = " + member);

        // Member의 비밀번호 확인
        if (!passwordEncoder.matches(request.getBody().getPw(), member.getPassword())) {
            throw new BadCredentialsException("잘못된 비밀번호입니다.");
        }

        return SignResponse.builder()
                .id(member.getId())
                .accountId(member.getAccountId())
                .email(member.getEmail())
                .roles(member.getRoles())
                //.refreshToken(jwtProvider.createToken(member.getAccountId(), member.getRoles()))
                .build();

    }

    // 회원가입
    public Boolean register(SignRequest request) throws Exception {
        try {
            Member member = Member.builder()
                    .accountId(request.getId())
                    .password(passwordEncoder.encode(request.getPw()))
                    .email(request.getEmail())
                    .build();

            // 권한 부여
            member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

            memberRepository.save(member);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }
}

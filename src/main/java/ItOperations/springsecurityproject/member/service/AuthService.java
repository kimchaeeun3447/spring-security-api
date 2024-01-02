package ItOperations.springsecurityproject.member.service;

import ItOperations.springsecurityproject.common.exception.CustomException;
import ItOperations.springsecurityproject.common.exception.ErrorCode;
import ItOperations.springsecurityproject.member.domain.Authority;
import ItOperations.springsecurityproject.member.domain.Member;
import ItOperations.springsecurityproject.member.dto.LoginRequest;
import ItOperations.springsecurityproject.member.dto.SignRequest;
import ItOperations.springsecurityproject.member.dto.SignResponse;
import ItOperations.springsecurityproject.member.repository.MemberRepository;
import ItOperations.springsecurityproject.security.JwtProvider;
import ItOperations.springsecurityproject.security.token.TokenDto;
import ItOperations.springsecurityproject.security.token.TokenService;

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
    private final TokenService tokenService;

    // 로그인 인증
    public SignResponse login(LoginRequest request) {
        // 계정 아이디로 Member 조회
        Member member = memberRepository.findByAccountId(request.getBody().getId()).orElseThrow(() ->
                new BadCredentialsException("잘못된 아이디입니다."));

        // Member의 비밀번호 확인
        if (!passwordEncoder.matches(request.getBody().getPw(), member.getPassword())) {
            throw new BadCredentialsException("잘못된 비밀번호입니다.");
        }

        // 리프레시토큰 발급
        member.setRefreshToken(tokenService.createRefreshToken(member));

        return SignResponse.builder()
                .id(member.getId())
                .accountId(member.getAccountId())
                .email(member.getEmail())
                .roles(member.getRoles())
                .token(TokenDto.builder()
                        .access_token(jwtProvider.createToken(member.getAccountId(), member.getRoles())) // 새 access 토큰 발급
                        .refresh_token(member.getRefreshToken()) // DB에 저장된 refresh 토큰
                        .build())
                .build();

    }

    // 회원가입
    public SignResponse register(SignRequest request) {
        try {
            Member member = Member.builder()
                    .accountId(request.getId())
                    .password(passwordEncoder.encode(request.getPw()))
                    .email(request.getEmail())
                    .build();

            // 권한 부여
            member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

            memberRepository.save(member);

            return SignResponse.builder()
                    .id(member.getId())
                    .accountId(member.getAccountId())
                    .email(member.getEmail())
                    .roles(member.getRoles())
                    .build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }

    // 사용자 정보 조회 API - 계정 아이디로 조회
    public SignResponse getMember(String accountId) {
        Member member = memberRepository.findByAccountId(accountId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        return new SignResponse(member);
    }
}

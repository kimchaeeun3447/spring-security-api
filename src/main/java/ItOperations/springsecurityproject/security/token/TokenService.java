package ItOperations.springsecurityproject.security.token;

import ItOperations.springsecurityproject.member.domain.Member;
import ItOperations.springsecurityproject.member.repository.MemberRepository;
import ItOperations.springsecurityproject.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    // refresh 토큰 생성 : Redis 내부에 [refreshToken:memberId : token] 형태로 저장
    public String createRefreshToken(Member member) {
        Token token = tokenRepository.save(
                Token.builder()
                        .id(member.getId())
                        .refresh_token(UUID.randomUUID().toString())
                        .expiration(150) // 로컬 테스트 용이를 위해 150초 후에 만료하게 설정
                        .build()
        );

        return token.getRefresh_token();
    }

    // refresh 토큰 유효성 검증
    public Token validRefreshToken(Member member, String refreshToken) throws Exception {
        // Member 고유 객체 id값으로 Token 조회
        Token token = tokenRepository.findById(member.getId()).orElseThrow(() ->
                new Exception("만료된 토큰입니다. 재로그인이 필요합니다.")
        );

        // 해당유저의 refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
        if (token.getRefresh_token() == null) {
            return null;
        } else {
            // refresh 토큰 만료가 10초 미만으로 남았다면 토큰 제거 후 재발급
            if(token.getExpiration() < 10) {
                tokenRepository.deleteById(member.getId());
                createRefreshToken(member);
            }

            // refresh 토큰 대조 - DB의 토큰과 요청받은 토큰이 같은지 비교, null 또는 토큰 반환
            if(!token.getRefresh_token().equals(refreshToken)) {
                return null;
            } else {
                return token;
            }
        }
    }

    // access, refresh 토큰을 받아서 access 토큰 재발급하기
    // access 토큰이 만료되었든 안되었든 재발급됨
    public TokenDto refreshAccessToken(TokenDto tokenDto) throws Exception {
        // access 토큰에서 계정 아이디 획득
        String accountId = jwtProvider.getAccount(tokenDto.getAccess_token());

        // 계정 아이디로 Member 조회
        Member member = memberRepository.findByAccountId(accountId).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정 아이디입니다.")
        );

        // DB에 저장된 리프레시 토큰과 대조하며 유효성 검증
        Token refreshToken = validRefreshToken(member, tokenDto.getRefresh_token());

        // 리프레시 토큰이 유효하다면 -> access 토큰 재발급한 후, refresh 토큰과 함께 DTO로 전달
        if (refreshToken != null) {
            return TokenDto.builder()
                    .access_token(jwtProvider.createToken(accountId, member.getRoles()))
                    .refresh_token(refreshToken.getRefresh_token())
                    .build();
        } else {
            // refreshToken 결과가 null이라면 -> 유효하지 않은 리프레시 토큰이니까 재발급 요청 필요
            throw new Exception("유효하지 않은 토큰입니다. 재로그인이 필요합니다.");
        }
    }
}

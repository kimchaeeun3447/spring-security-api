package ItOperations.springsecurityproject.security;

import ItOperations.springsecurityproject.member.domain.Member;
import ItOperations.springsecurityproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/* Spring Security의 UserDetail 인증 정보를 토대로 유저 정보 가져오기 */
@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {

        Member member = memberRepository.findByAccountId(accountId).orElseThrow(
                () -> new UsernameNotFoundException("[JpaUserDetailsService] Invalid Authentication")
        );

        return new CustomUserDetails(member);
    }
}

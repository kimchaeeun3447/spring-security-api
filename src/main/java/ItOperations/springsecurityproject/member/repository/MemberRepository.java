package ItOperations.springsecurityproject.member.repository;

import ItOperations.springsecurityproject.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 계정 아이디로 Member 객체 검색
    Optional<Member> findByAccountId(String accountId);
}

package ItOperations.springsecurityproject.security.token;

import org.springframework.data.repository.CrudRepository;

// Redis 와의 연산을 수행하기 위해 CrudRepository 확장
public interface TokenRepository extends CrudRepository<Token, Long> {
}

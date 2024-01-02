package ItOperations.springsecurityproject.security.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

/* Token 클래스
- Redis에 해시 데이터 타입으로 저장
- TTL 경과 시 Redis에서 자동적으로 삭제됨
*  */

@Getter
@RedisHash("refreshToken")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    @Id
    @JsonIgnore
    private Long id;          // Member 고유 객체 id

    private String refresh_token;

    @TimeToLive(unit = TimeUnit.SECONDS)    // Token 유효기간 TTL로 결정 - 초단위, 지정된 시간 경과시 자동 삭제
    private Integer expiration;

    // 토큰 TTL 연장
    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }
}

# spring-security-api
인증을 통해 DB 내 특정 값을 회신하는 OPEN API

<br>

### Commit Convention
- feat: 기능 (feature)
- fix: 버그 수정
- docs: 문서 작업 (documentation)
- style: 포맷팅, 세미콜론 누락 등.
- refactor: 리팩터링
- test: 테스트
- chore: 관리(maintain), 핵심 내용은 아닌 잡일 등

### Work Flow
1. Jira에서 작업 생성 - Jira 티켓 확인
2. 깃허브 이슈 생성
3. 브랜치생성 : feat/#이슈번호/티켓, 해당 브랜치에서 작업 후 commit ( commit 메시지엔 issue 번호 명시)
4. develop 브랜치로 pull request할 때 Jira 티켓(이슈 키)들 명시하기 -> 지라 작업에 자동으로 커밋 기록됨
5. 모든 작업 끝나면 main으로 merge


### 환경 구성

- 개발환경 : Spring Boot 3.2.1, Java 21, JDK21
- 빌드 관리 : Gradle
- Dependency : Spring Web, Spring Security, JPA, Lombok, H2



### 폴더링
```
└── src
  └── main
   ├── java
   │   └── com
   │       └── example
   │           └── springsecurityproject
   │               ├── ItOperationsApplication.java
   │               ├── common
   │               │    ├── exception
   │               │    ├── ApiUtils.java
   │               │    └── CommonResponse.java
   │               ├── member
   │               │    ├── controller
   │               │    ├── domain
   │               │    ├── dto
   │               │    ├── repository
   │               │    └── service
   │               └── security
   │                   ├── token
   │                   │       ├── Token.java
   │                   │       ├── TokenDto.java
   │                   │       ├── TokenRepository.java
   │                   │       └── TokenService.java
   │ 	 ├── CustomUserDetails.java
   │                   ├──  JpaUserDetailsService.java
   │                   ├── JwtAuthenticationFilter.java
   │                   ├── JwtProvider.java
   │                   └── SecurityConfig.java
   └── resources
       └── application.yml
```

### 실행방법
1) intelliJ 환경변수 설정으로 jwt secret key 등록 필요 -> Edit Configuration

jwt.secret.key=[jwt secret 키]

<br>

2) build.gradle 실행

    <br>

3) 로그인 API 사용 시, 리프레시 토큰이 발급되어 Redis에 저장되어야 하기 때문에, 로컬에서 Redis 서버를 실행시켜야한다.
Redis-x64-3.0.504를 다운받은 후, redis-server.exe를 실행시킨 후, redis-cli.exe로 Redis 확인 가능
<br>


4) 로컬 H2 데이터베이스 확인 방법
http://localhost:8080/h2-console 로 접속 후, 유저명에 teamIT / 비밀번호 공란으로 두고 Connect

* In-Memory로 설정했기 때문에, 실행 할 때마다 데이터베이스가 새로 생성됨
<br>

5) API 사용
- 회원가입 API로 회원 생성 (USER 권한 부여됨)
- 로그인 API로 Access, Refresh 토큰 획득
- Access 토큰 만료 시, 토큰 재발급 API로 재발급
- Refresh 토큰 만료 시, 재로그인 후 Access, Refresh 재발급
- 사용자 조회 API 사용시, 헤더의 Authorization에 Bearer 토큰으로 Access 토큰을 넣어야함

<br>

6) redis-cli.exe에서 Redis 해시 키 형태의 Refresh 토큰 목록 확인법

- 레디스 서버에 있는 모든 key 검색 : keys *
    - refreshToken:[회원 고유 id 값] 형태로 저장됨

127.0.0.1:6379> keys *

1. "refreshToken:3"
2. "refreshToken"
3. "refreshToken:1"

<br>
- 특정 키의 TTL 확인 : ttl ttl refreshToken:1
    - 현재 단위는 초로 설정되어있음

127.0.0.1:6379> ttl refreshToken:1
(integer) 286
127.0.0.1:6379> ttl refreshToken:1
(integer) 282
127.0.0.1:6379> ttl refreshToken:1
(integer) 270
127.0.0.1:6379> ttl refreshToken:3

<br>
- 특정 키( "refreshToken:1”) 해시(Hash)에 저장된 모든 필드들의 키를 반환 : HKEYS refreshToken:1

127.0.0.1:6379> HKEYS refreshToken:1

1. "_class"
2. "expiration"  // TTL
3. "id"  // Member 고유 id 값
4. "refresh_token"  // 리프레시 토큰

<br>
- 특정 키의 리프레시 토큰 확인 : HGET refreshToken:1 refresh_token

127.0.0.1:6379> HGET refreshToken:1 refresh_token

<br>
- TTL이 경과되면 자동으로 키가 삭제됨.

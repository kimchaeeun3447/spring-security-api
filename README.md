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

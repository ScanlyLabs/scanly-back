# Scanly Backend

QR 기반 디지털 명함 서비스 백엔드 API 서버

## 소개
Scanly는 QR 코드를 활용한 디지털 명함 서비스입니다. 한 번의 스캔으로 명함을 교환하고, 저장된 명함을 그룹/태그로 관리할 수 있습니다.

### 핵심 기능
- **명함 생성/관리**: 디지털 명함 생성 및 QR 코드 자동 생성
- **QR 스캔**: 상대방 QR 코드 스캔으로 명함 조회
- **명함첩**: 저장한 명함을 그룹별로 분류 및 관리
- **명함 교환 요청**: 명함 교환 요청 시 상대방에게 알림 전송
- **알림**: 명함 교환 알림 지원

## 성능 및 안정성

### 캐싱
- 명함 조회: Redis 캐싱 적용 (TTL 1시간)
- 명함 수정/삭제 시 캐시 무효화

### Rate Limiting
| 적용 대상 | 제한                           | 목적           |
|----------|------------------------------|--------------|
| 명함 교환 API | 사용자별 시간당 15회 제한              | 전체적인 스팸/봇 방지 |
| 동일 수신자 교환 | 발신자 - 수신자 쌍 일일 3회 제한 (자정 리셋) | 특정인 대상 스팸 방지 |
- Redis 기반 Fixed Window 알고리즘 적용
- AOP 어노테이션(`@RateLimiter`) 기반으로 적용
  
### Redis 장애 대응
| 적용 대상 | 장애 대응 전략 | 동작 방식 |
|----------|------------|------------|
| 명함 조회 캐시 | Spring CacheErrorHandler | Redis 장애 시 캐시 스킵 -> DB 직접 조회 |
| 동일 수신자 교환 | Resilience4j Circuit Breaker | Redis 장애 시 fallback → 요청 허용 (가용성 우선) |
-  Rate Limiting (Circuit Breaker)
      - 장애 감지: 최근 10회 호출 중 실패율 50% 이상 시 OPEN
      - OPEN 상태: 30초간 Redis 호출 없이 즉시 fallback 실행
      - 복구 테스트: 30초 후 HALF_OPEN → 3회 중 2회 이상 성공 시 CLOSED 복귀 

## Tech Stack
| Category  | Technology |
|-----------|------------|
| Language  | Java 21 |
| Framework | Spring Boot 4.0.1 |
| Security  | Spring Security, JWT |
| Database  | PostgreSQL, Spring Data JPA |
| Cache     | Redis |
| Storage   | AWS S3 |
| Docs      | Swagger (springdoc-openapi) |
| Build     | Gradle |
| QR Code   | ZXing |
| CI/CD     | GitHub Actions |
| Infra     | AWS EC2, Docker |

## Architecture

### Layered Architecture + DDD
```
Presentation (Controller, Request/Response DTO)
      ↓
Application (Service, Command/Info DTO)
      ↓
Domain (Domain Model, Repository Interface, Business Logic)
      ↑
Infrastructure (JPA Entity, Repository Impl, External API)
```

### Project Structure
```
src/main/java/scanly/io/scanly_back
├── common/
│   ├── config/          # 설정 (Security, JPA, Redis, S3)
│   ├── exception/       # 예외 처리 (ErrorCode, CustomException)
│   ├── response/        # 공통 응답 (ApiResponse, PageResponse)
│   ├── entity/          # 공통 엔티티 (BaseEntity)
│   ├── domain/          # 공통 도메인 (BaseDomain)
│   ├── service/         # 공통 서비스
│   └── util/            # 유틸리티
│
├── auth/
│   ├── presentation/    # AuthController
│   ├── application/     # AuthService
│   ├── domain/          # Auth Domain
│   └── infrastructure/  # JWT, Token 관련 구현체
│
├── member/
│   ├── presentation/    # MemberController
│   ├── application/     # MemberService
│   ├── domain/          # Member
│   └── infrastructure/  # MemberEntity, MemberRepositoryImpl
│
├── card/
│   ├── presentation/    # CardController
│   ├── application/     # CardService
│   ├── domain/          # Card, SocialLink
│   └── infrastructure/  # CardEntity, SocialLinkEntity
│
├── cardbook/
│   ├── presentation/    # CardBookController, GroupController
│   ├── application/     # CardBookService, GroupService
│   ├── domain/          # CardBook, Group
│   └── infrastructure/  # CardBookEntity, GroupEntity
│
└── notification/
    ├── presentation/    # NotificationController, PushTokenController
    ├── application/     # NotificationService, PushTokenService
    ├── domain/          # Notification, PushToken
    └── infrastructure/  # NotificationEntity, PushTokenEntity
```

## 📌 API 문서 (Swagger)
- http://13.124.208.135:8080/swagger-ui/index.html

※ 포트폴리오 시연을 위해 Swagger를 외부에 공개하였습니다.  
※ 일부 API는 JWT 인증이 필요합니다.  


### 🔐 API 테스트 방법
1. 로그인 API(`/api/auth/login`) 실행
2. 응답으로 받은 accessToken 복사
3. Swagger 상단 [Authorize] 버튼 클릭
4. value 입력칸에 accessToken 입력 후 인증


### 👤 테스트 계정
- ID: test
- PW: test1234


### 🧪 명함 교환 테스트 시나리오 (Swagger)

#### 사전 준비
- 테스트 계정 2개 필요 (`test`, `test2`)
- 각 계정에 명함이 생성되어 있어야 함

#### 테스트 순서
1. **A 로그인** → `/api/auth/v1/login` (ID: test)
2. **B 명함 조회** → `/api/cards/v1/member/{loginId}` (loginId: test2)
3. **명함 교환 요청** → `/api/cardbooks/v1/exchange` (cardId: test2의 cardId)
   - `test2`에게 명함 교환 요청 알림 전송됨 (응답에서 exchangeId 확인)
4. **B 로그인** → `/api/auth/v1/login` (ID: test2)
5. **명함 교환 수락** → `/api/cardbooks/v1/exchange/accept` (exchangeId 입력)
   - `test`의 명함이 `test2`의 명함첩에 자동 저장됨
6. **명함첩 확인** → `/api/cardbooks/v1`

## API Endpoints

### 인증 (Auth)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/v1/login` | 로그인 |
| POST | `/api/auth/v1/logout` | 로그아웃 |
| POST | `/api/auth/v1/reissue` | 토큰 재발급 |

### 회원 (Member)
| Method | Endpoint                                     | Description             |
|--------|----------------------------------------------|-------------------------|
| POST   | `/api/members/v1/sign-up`                    | 회원가입                    |
| GET    | `/api/members/v1/me`                         | 내 정보 조회                 |
| POST   | `/api/members/v1/me/update`                  | 내 정보 수정                 |
| POST   | `/api/members/v1/me/password`                | 비밀번호 변경                 |
| GET   | `/api/members/v1/me/check-login-id/{loginId}` | 로그인 아이디 중복 체크(사용 가능 여부) |
| GET   | `/api/members/v1/me/withdrawal`                        | 회원 탈퇴                   |

### 명함 (Card)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/cards/v1` | 명함 생성 |
| GET | `/api/cards/v1/me` | 내 명함 조회 |
| GET | `/api/cards/v1/member/{loginId}` | 회원 명함 조회 |
| POST | `/api/cards/v1/me/update` | 내 명함 수정 |
| POST | `/api/cards/v1/me/delete` | 내 명함 삭제 |

### 명함첩 (CardBook)
| Method | Endpoint | Description |
|--------|----------|-------|
| POST | `/api/cardbooks/v1` | 명함 저장 |
| POST | `/api/cardbooks/v1/exchange` | 명함 교환 요청 |
| POST | `/api/cardbooks/v1/exchange/accept` | 명함 교환 수락 |
| GET | `/api/cardbooks/v1` | 명함첩 목록 조회 |
| GET | `/api/cardbooks/v1/{id}` | 명함첩 상세 조회 |
| GET | `/api/cardbooks/v1/exists` | 명함첩 존재 여부 확인 |
| POST | `/api/cardbooks/v1/{id}/group` | 명함첩 그룹 수정 |
| POST | `/api/cardbooks/v1/{id}/memo` | 명함첩 메모 수정 |
| POST | `/api/cardbooks/v1/{id}/favorite` | 명함첩 즐겨찾기 수정 |
| POST | `/api/cardbooks/v1/{id}/delete` | 명함첩 삭제 |
| POST | `/api/cardbooks/v1/{id}/refresh-snapshot` | 프로필 스냅샷 최신화 |

### 태그 (Tag)
| Method | Endpoint                   | Description |
|--------|----------------------------|-------------|
| POST | `/api/tags/v1`             | 태그 저장       |
| POST | `/api/tags/v1/{id}/update` | 태그 수정       |
| POST | `/api/tags/v1/{id}/delete` | 태그 삭제       |

### 그룹 (Group)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/groups/v1` | 그룹 생성 |
| GET | `/api/groups/v1` | 그룹 목록 조회 |
| POST | `/api/groups/v1/{id}/rename` | 그룹명 수정 |
| POST | `/api/groups/v1/reorder` | 그룹 순서 변경 |
| POST | `/api/groups/v1/{id}/delete` | 그룹 삭제 |

### 알림 (Notification)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/notifications/v1` | 알림 목록 조회 |
| GET | `/api/notifications/v1/unread-count` | 안 읽은 알림 수 조회 |
| POST | `/api/notifications/v1/{id}/read` | 알림 읽음 처리 |

### 푸시 토큰 (PushToken)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/push-tokens/v1` | 푸시 토큰 등록 |

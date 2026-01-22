# Scanly Backend

QR 기반 디지털 명함 서비스 백엔드 API 서버

## 소개
Scanly는 QR 코드를 활용한 디지털 명함 서비스입니다. 한 번의 스캔으로 명함을 교환하고, 저장된 명함을 그룹/태그로 관리할 수 있습니다.

### 핵심 기능
- **명함 생성/관리**: 디지털 명함 생성 및 QR 코드 자동 생성
- **QR 스캔**: 상대방 QR 코드 스캔으로 명함 조회
- **명함첩**: 저장한 명함을 그룹/태그로 분류 및 관리
- **상호 교환**: 명함 저장 시 내 명함도 상대방에게 전송

## Tech Stack
| Category | Technology |
|----------|------------|
| Language | Java 21 |
| Framework | Spring Boot 4.0.1 |
| Security | Spring Security, JWT |
| Database | PostgreSQL, Spring Data JPA |
| Cache | Redis |
| Storage | AWS S3 |
| Docs | Swagger (springdoc-openapi) |
| Build | Gradle |

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
│   ├── response/        # 공통 응답 (ApiResponse)
│   ├── entity/          # 공통 엔티티 (BaseEntity)
│   └── domain/          # 공통 도메인 (BaseDomain)
│
├── member/
│   ├── presentation/    # MemberController, AuthController
│   ├── application/     # MemberService, AuthService
│   ├── domain/          # Member, MemberRepository
│   └── infrastructure/  # MemberEntity, MemberRepositoryImpl
│
├── card/
│   ├── presentation/    # CardController
│   ├── application/     # CardService
│   ├── domain/          # Card, SocialLink, CardRepository
│   └── infrastructure/  # CardEntity, SocialLinkEntity, CardRepositoryImpl
│
└── cardbook/
    ├── presentation/    # CardBookController, GroupController
    ├── application/     # CardBookService, GroupService
    ├── domain/          # CardBook, Group, Tag
    └── infrastructure/  # CardBookEntity, GroupEntity, TagEntity
```

## API Endpoints

### 인증 (Auth)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/members/v1/signup` | 회원가입 |
| POST | `/api/auth/v1/login` | 로그인 |
| POST | `/api/auth/v1/refresh` | 토큰 갱신 |
| POST | `/api/auth/v1/logout` | 로그아웃 |

### 명함 (Card)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/cards/v1` | 명함 생성 |
| GET | `/api/cards/v1/me` | 내 명함 조회 |
| POST | `/api/cards/v1/update` | 명함 수정 |
| POST | `/api/cards/v1/delete` | 명함 삭제 |

### 명함첩 (CardBook)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/cardbooks/v1` | 명함 저장 |

### 그룹 (Group)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/groups/v1` | 그룹 생성 |
| GET | `/api/groups/v1` | 그룹 목록 조회 |
| POST | `/api/groups/v1/{id}/rename` | 그룹명 수정 |
| POST | `/api/groups/v1/reorder` | 그룹 순서 변경 |
| POST | `/api/groups/v1/{id}/delete` | 그룹 삭제 |

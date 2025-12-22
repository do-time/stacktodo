# CLAUDE.md

이 파일은 Claude Code (claude.ai/code)가 이 저장소의 코드를 작업할 때 가이드를 제공합니다.

## 프로젝트 개요

StackTodo Backend는 Java 21로 빌드된 Spring Boot 3.4.9-SNAPSHOT 애플리케이션으로, 헥사고날 아키텍처(포트와 어댑터)를 따릅니다. 워크스페이스 협업, OAuth2 인증, Kafka 기반 알림을 갖춘 작업 관리 시스템을 제공합니다.

## 빌드 및 개발 명령어

### 애플리케이션 실행
```bash
# 필수 서비스 실행 (MySQL, Kafka)
docker-compose up -d

# 애플리케이션 실행
./gradlew bootRun

# 프로젝트 빌드
./gradlew build

# 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests "FullyQualifiedTestClassName"
```

### 데이터베이스
- MySQL 8이 포트 3306에서 실행됩니다 (Docker Compose 사용)
- 데이터베이스 이름: `stacktodo`
- 자격증명은 `docker-compose.yml`에 있습니다
- JPA DDL 모드: `create-drop` (시작 시 스키마 재생성)

### Kafka
- Kafka는 포트 29092 (localhost)에서 실행됩니다
- 알림 메시징에 사용됩니다
- `docker-compose.yml`에 구성되어 있습니다

## 아키텍처

### 헥사고날 아키텍처 (포트와 어댑터)

코드베이스는 비즈니스 도메인별로 구성된 엄격한 헥사고날 아키텍처 패턴을 따릅니다. 각 도메인 모듈은 다음을 포함합니다:

```
domain/
├── adapter/
│   ├── in/web/          # 인바운드 REST 컨트롤러 및 DTO
│   └── out/persistence/ # 아웃바운드 JPA 리포지토리 및 엔티티
├── application/
│   ├── command/         # 작업을 위한 커맨드 객체
│   ├── service/         # 유스케이스 구현
│   └── port/
│       ├── in/          # 인바운드 포트 (유스케이스 인터페이스)
│       └── out/         # 아웃바운드 포트 (리포지토리 인터페이스)
├── domain/model/        # 순수 도메인 모델
├── exception/           # 도메인별 예외 및 핸들러
└── mapper/              # Entity-Domain 매퍼
```

### 도메인 모듈

- **member**: 사용자 관리 및 OAuth2 인증
- **workspace**: 멤버 역할이 있는 팀 워크스페이스
- **category**: 범위(개인/워크스페이스/커뮤니티)가 있는 작업 카테고리화
- **task**: 상태 추적을 포함한 작업 관리
- **tasklist**: 작업 목록 그룹화
- **notification**: Kafka 기반 알림 시스템
- **infrastructure**: 공통 관심사 (보안, JWT, OAuth2, Jasypt)
- **common**: 공유 유틸리티 및 기본 엔티티

### 주요 아키텍처 원칙

1. **도메인 모델은 순수합니다**: `domain/model/`의 도메인 객체는 프레임워크 의존성을 포함하지 않습니다
2. **포트는 계약을 정의합니다**:
   - 인바운드 포트 (`port/in/`)는 서비스가 구현하는 인터페이스입니다 (유스케이스)
   - 아웃바운드 포트 (`port/out/`)는 어댑터가 구현하는 인터페이스입니다
3. **작업에는 커맨드를 사용합니다**: 모든 상태 변경은 유스케이스에 전달되는 커맨드 객체를 사용합니다
4. **어댑터는 분리되어 있습니다**:
   - 웹 컨트롤러 (`adapter/in/web/`)는 HTTP와 DTO를 처리합니다
   - 퍼시스턴스 어댑터 (`adapter/out/persistence/`)는 데이터베이스 작업을 처리합니다
5. **도메인별 예외 핸들러**: 각 도메인은 전용 예외 핸들러를 가집니다

### 데이터 흐름 패턴

1. 컨트롤러가 HTTP 요청 수신 → 커맨드 객체 생성
2. 컨트롤러가 UseCase 인터페이스 호출 (인바운드 포트)
3. 서비스가 UseCase 구현 → 아웃바운드 포트 호출
4. 리포지토리 어댑터가 아웃바운드 포트 구현 → 데이터베이스와 상호작용
5. 매퍼가 Entity ↔ Domain Model ↔ DTO 간 변환 수행

## 보안 및 인증

### OAuth2 구성
- 제공자: Google, GitHub
- 커스텀 OAuth2 사용자 서비스: `PrincipalOauth2UserService`
- 자격증명은 `application.yml`에 있습니다 (외부화 권장)

### JWT 인증
- `application.yml`의 `security.jwt.secret`을 통해 시크릿 키 구성
- Spring Security OAuth2 Resource Server가 JWT 디코딩 처리
- `JwtKeyHolder`를 통한 키 관리

### Jasypt 암호화
- 민감한 구성을 위한 비밀번호 기반 암호화
- Bean: `jasyptStringEncryptor`
- `application.yml`에 비밀번호 구성

### 보안 구성
- 대부분의 엔드포인트는 현재 개발용으로 `permitAll()`로 설정되어 있습니다
- OAuth2 로그인은 성공 시 `/`로 리다이렉트됩니다
- CORS는 API 테스트를 위해 완전히 개방되어 있습니다 (운영 환경에서는 제한 필요)

## 데이터베이스 규칙

- **테이블 명명**: 복수형 사용 (예: `categories`, `category` 아님)
- **엔티티 기본 클래스**: `BaseEntity`가 공통 감사 필드를 제공합니다
- **UUID 기본 키**: 대부분의 엔티티는 식별자로 UUID를 사용합니다
- **JPA 감사**: `@EntityListeners(AuditingEntityListener.class)`를 통해 활성화됩니다
- **컬럼 명명**: 데이터베이스에서는 snake_case, Java에서는 camelCase

## 테스팅

- **E2E 테스트 어노테이션**: 통합 테스트를 위한 `@E2eTest`
  - 랜덤 포트로 전체 Spring 컨텍스트 시작
  - 테스트 데이터를 위한 `TestFixtureConfiguration` 포함
  - 패턴으로 JPA 리포지토리 활성화: `io.app.stacktodobe.**.persistence.repository`
- 테스트는 `src/test/java/io/app/stacktodobe/`에 위치합니다

## 중요 사항

### 새로운 기능 추가 시
1. 기존 헥사고날 아키텍처 구조를 따릅니다
2. 도메인 모델을 먼저 생성합니다 (프레임워크 의존성 없음)
3. 작업을 위한 커맨드 객체를 정의합니다
4. `port/in/`에 인바운드 포트 (유스케이스 인터페이스)를 생성합니다
5. 외부 의존성을 위해 `port/out/`에 아웃바운드 포트를 생성합니다
6. `application/service/`에 서비스를 구현합니다
7. `adapter/in/web/`에 웹 어댑터 (컨트롤러)를 생성합니다
8. `adapter/out/persistence/`에 퍼시스턴스 어댑터를 생성합니다
9. 필요한 경우 예외 핸들러를 추가합니다

### 명명 규칙
- Commands: `{동사}{엔티티}Command` (예: `CreateCategoryCommand`)
- Use Cases: `{동사}{엔티티}UseCase` (예: `CreateCategoryUseCase`)
- Services: `{동사}{엔티티}Service` (예: `CreateCategoryService`)
- Ports: `{동사}{엔티티}Port` (예: `CreateCategoryPort`)
- Entities: `{엔티티}Entity` (예: `CategoryEntity`)
- Controllers: `{엔티티}{Command|Query}Controller`

### 도메인 주도 패턴
- 도메인 모델에 팩토리 메서드를 사용합니다 (예: `Category.ofPersonal()`)
- 가능한 경우 비즈니스 로직을 도메인 모델에 유지합니다
- 복잡한 속성에는 값 객체를 사용합니다 (예: member 도메인의 `Password`)
- 명령과 조회 작업을 분리합니다 (CQRS-lite)

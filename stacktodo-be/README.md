# StackTodo Backend

워크스페이스 협업, OAuth2 인증, 실시간 알림 기능을 갖춘 협업 작업 관리 시스템입니다.

## 사전 요구사항

- **Java 21** 이상
- **Docker** 및 **Docker Compose** (MySQL과 Kafka 실행용)
- **Gradle** (래퍼 포함)

## 기술 스택

- **프레임워크**: Spring Boot 3.4.9-SNAPSHOT
- **언어**: Java 21
- **데이터베이스**: MySQL 8
- **보안**: Spring Security with OAuth2 (Google, GitHub)
- **인증**: JWT
- **메시징**: Apache Kafka
- **ORM**: Spring Data JPA with Hibernate
- **암호화**: Jasypt (설정 암호화)

## 빠른 시작

### 1. 저장소 클론

```bash
git clone <repository-url>
cd stacktodo-be
```

### 2. 필수 서비스 실행

Docker Compose를 사용하여 MySQL과 Kafka를 실행합니다:

```bash
docker-compose up -d
```

다음 서비스가 시작됩니다:
- **MySQL 8** - 포트 `3306`
  - 데이터베이스: `stacktodo`
  - 사용자: `stackuser`
  - 비밀번호: `1234!`
- **Kafka** - 포트 `29092`

### 3. OAuth2 설정 (선택사항)

애플리케이션은 `src/main/resources/application.yml`에 임시 OAuth2 자격증명이 포함되어 있습니다. 실제 OAuth2 인증을 사용하려면:

**Google OAuth2:**
1. [Google Cloud Console](https://console.cloud.google.com/)로 이동
2. OAuth2 자격증명 생성
3. `application.yml` 업데이트:
   ```yaml
   spring:
     security:
       oauth2:
         client:
           registration:
             google:
               client-id: YOUR_GOOGLE_CLIENT_ID
               client-secret: YOUR_GOOGLE_CLIENT_SECRET
   ```

**GitHub OAuth2:**
1. GitHub 설정 → Developer settings → OAuth Apps로 이동
2. 새 OAuth App 생성
3. `application.yml` 업데이트:
   ```yaml
   spring:
     security:
       oauth2:
         client:
           registration:
             github:
               clientId: YOUR_GITHUB_CLIENT_ID
               clientSecret: YOUR_GITHUB_CLIENT_SECRET
   ```

### 4. 애플리케이션 실행

```bash
./gradlew bootRun
```

애플리케이션은 `http://localhost:8080`에서 실행됩니다.

## 개발

### 프로젝트 빌드

```bash
./gradlew build
```

### 테스트 실행

```bash
# 모든 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests "io.app.stacktodobe.category.CategoryTest"
```

### 데이터베이스 관리

애플리케이션은 JPA의 `ddl-auto: create-drop`을 사용합니다:
- 애플리케이션 **시작 시마다 스키마가 재생성**됩니다
- 애플리케이션 종료 시 모든 데이터가 삭제됩니다
- 개발 환경에만 적합합니다

동작 방식을 변경하려면 `application.yml`을 수정하세요:
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # 또는 운영 환경에서는 'validate'
```

### 실행 중인 서비스 확인

```bash
# Docker 컨테이너 확인
docker-compose ps

# MySQL 로그 확인
docker logs stacktodo-mysql

# Kafka 로그 확인
docker-compose logs kafka
```

### 서비스 중지

```bash
docker-compose down
```

## 설정

### 애플리케이션 속성

`src/main/resources/application.yml`의 주요 설정:

- **서버 포트**: `8080`
- **JWT 시크릿**: `security.jwt.secret`
- **Jasypt 비밀번호**: `jasypt.encryptor.password`
- **데이터베이스**: Docker Compose를 통해 설정
- **OAuth2**: Google 및 GitHub 클라이언트 자격증명

### 환경 변수

환경 변수를 사용하여 설정을 재정의할 수 있습니다:

```bash
# 예시: 서버 포트 변경
export SERVER_PORT=9090
./gradlew bootRun

# 예시: JWT 시크릿 설정
export SECURITY_JWT_SECRET=your-secret-key
./gradlew bootRun
```

## API 엔드포인트

### 공개 엔드포인트

- `POST /api/v1/members/signup` - 사용자 등록
- `POST /api/v1/members/issueToken` - JWT 토큰 발급
- `GET /api/v1/workspaces/**` - 워크스페이스 작업
- `GET /api/v1/tasks/**` - 작업 관리

### OAuth2 로그인

- Google: `http://localhost:8080/oauth2/authorization/google`
- GitHub: `http://localhost:8080/oauth2/authorization/github`

로그인 성공 후 `/`로 리다이렉트됩니다.

## 아키텍처

이 프로젝트는 **헥사고날 아키텍처**(포트와 어댑터 패턴)를 따릅니다. 자세한 아키텍처 문서는 [CLAUDE.md](CLAUDE.md)를 참조하세요.

주요 아키텍처 개념:
- **도메인 주도 설계**와 순수한 도메인 모델
- 포트와 어댑터를 통한 **관심사의 분리**
- 명령과 조회 작업을 분리하는 **CQRS-lite**
- 비즈니스 도메인별로 구성된 **모듈 구조**

## 프로젝트 구조

```
src/main/java/io/app/stacktodobe/
├── category/          # 작업 카테고리화
├── common/            # 공유 유틸리티
├── hashtag/           # 해시태그 기능
├── infrastructure/    # 공통 관심사 (보안, JWT, 설정)
├── member/            # 사용자 관리 및 인증
├── notification/      # Kafka 기반 알림
├── task/              # 작업 관리
├── tasklist/          # 작업 목록 그룹화
└── workspace/         # 팀 워크스페이스 협업
```

각 도메인 모듈은 헥사고날 아키텍처를 따릅니다:
```
domain/
├── adapter/in/web/              # REST 컨트롤러
├── adapter/out/persistence/     # JPA 리포지토리
├── application/
│   ├── command/                 # 커맨드 객체
│   ├── service/                 # 유스케이스 구현
│   └── port/in|out/             # 포트 인터페이스
├── domain/model/                # 순수 도메인 모델
└── exception/                   # 도메인 예외
```

## 문제 해결

### 포트가 이미 사용 중인 경우

포트 8080이 이미 사용 중이라면:
```bash
# application.yml에서 포트 변경 또는 환경 변수 사용
export SERVER_PORT=9090
./gradlew bootRun
```

### MySQL 연결 문제

```bash
# MySQL 컨테이너 재시작
docker-compose restart db

# MySQL 실행 여부 확인
docker-compose ps
```

### Kafka 연결 문제

```bash
# Kafka 재시작
docker-compose restart kafka

# 애플리케이션 시작 전 Kafka 준비 상태 확인
docker-compose logs kafka
```

### 클린 빌드

```bash
# 클린 후 재빌드
./gradlew clean build
```

## 기여하기

새로운 기능 추가 시:

1. 헥사고날 아키텍처 패턴 준수
2. 프레임워크 의존성이 없는 도메인 모델 작성
3. 구현체보다 포트(인터페이스)를 먼저 정의
4. 상태 변경 시 커맨드 객체 사용
5. 적절한 예외 처리 추가
6. `@E2eTest` 어노테이션을 사용하여 테스트 작성

자세한 가이드라인은 [CLAUDE.md](CLAUDE.md)를 참조하세요.

## 라이선스

[라이선스를 여기에 추가하세요]

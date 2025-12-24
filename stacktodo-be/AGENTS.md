# Repository Guidelines

## Project Structure & Module Organization
- 소스 코드: `src/main/java/io/app/stacktodobe/` (도메인 모듈: `member`, `workspace`, `task` 등).
- 테스트: `src/test/java/io/app/stacktodobe/` (`@E2eTest`는 통합 테스트용).
- 리소스/설정: `src/main/resources/` (주요 설정은 `application.yml`).
- 빌드 산출물: `build/`.
- 아키텍처: 헥사고날(ports/adapters). 일반 레이아웃:
  `adapter/in/web`, `adapter/out/persistence`, `application/command`, `application/service`, `application/port/in|out`, `domain/model`, `exception`, `mapper`.

## Build, Test, and Development Commands
- `docker-compose up -d`로 MySQL(3306)과 Kafka(29092) 실행.
- `./gradlew bootRun`으로 `http://localhost:8080`에서 앱 실행.
- `./gradlew build`로 전체 빌드 수행.
- `./gradlew test`로 전체 테스트 실행.
- `./gradlew test --tests "FullyQualifiedTestClassName"`로 특정 테스트 클래스 실행.
- `docker-compose down`으로 서비스 종료.

## Coding Style & Naming Conventions
- Java 21, Spring Boot 3.4.x; 표준 Java/Spring 컨벤션 준수.
- 들여쓰기: 공백 4칸, 탭 사용 금지.
- 명명 규칙:
  - Commands: `{동사}{엔티티}Command` (예: `CreateCategoryCommand`).
  - Use cases: `{동사}{엔티티}UseCase`.
  - Services: `{동사}{엔티티}Service`.
  - Ports: `{동사}{엔티티}Port`.
  - Entities: `{엔티티}Entity`.
  - Controllers: `{엔티티}{Command|Query}Controller`.
- DB: 테이블은 복수형 + snake_case, Java 필드는 camelCase.

## Testing Guidelines
- 주요 프레임워크: Spring Boot Test + JUnit 5.
- 통합 테스트는 `@E2eTest` 사용 및 전체 Spring 컨텍스트 기동.
- 테스트는 도메인별로 배치: `src/test/java/io/app/stacktodobe/{domain}`.

## Commit & Pull Request Guidelines
- 커밋 메시지는 `type: summary` 형식(예: `refactor: ...`, `docs: ...`).
- PR에는 목적, 영향 범위(모듈), 실행한 테스트, 관련 이슈/티켓을 포함.

## Security & Configuration Tips
- 시크릿(JWT, OAuth2, Jasypt)은 `application.yml`에 있으며, 로컬에서는 환경 변수 사용 권장.
- JPA `ddl-auto` 기본값은 `create-drop`이며, 실행마다 스키마가 재생성됨.

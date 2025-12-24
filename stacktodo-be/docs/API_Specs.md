# API 명세서

이 문서는 현재 코드 기준의 API 요청/응답 스키마를 정리합니다. 컨트롤러와 DTO 변경 시 함께 업데이트하세요.

## 공통 규칙
- Base URL: `http://localhost:8080`
- Content-Type: `application/json`
- 인증: Bearer JWT. 아래에서 Principal을 사용하는 API는 인증이 필요합니다.
- 날짜/시간 포맷: `LocalDate` = `YYYY-MM-DD`, `LocalTime` = `HH:mm:ss`, `YearMonth` = `YYYY-MM`.
- Enum 값:
  - `CategoryScope`: `PERSONAL`, `WORKSPACE`, `COMMUNITY`
  - `TaskStatus`: `PENDING`, `IN_PROGRESS`, `COMPLETED`

## 인증/멤버
### POST /api/v1/members/signup
- 인증: 없음
- 설명: 신규 회원 등록
- Request DTO: `CreateMemberRequest`

| 필드 | 타입 | 필수 | 제약 | 설명 |
| --- | --- | --- | --- | --- |
| email | String | Y | - | 이메일 |
| password | String | Y | - | 비밀번호 |
| username | String | N | - | 사용자명 |
| profileImage | String | N | - | 프로필 이미지 URL |
| phoneNumber | String | N | - | 전화번호 |

- Response: `204 No Content`

### POST /api/v1/members/issueToken
- 인증: 없음
- 설명: 로그인 후 JWT 토큰 발급
- Request DTO: `IssueTokenCommand`

| 필드 | 타입 | 필수 | 제약 | 설명 |
| --- | --- | --- | --- | --- |
| email | String | Y | 이메일 형식, 최대 100자 | 로그인 이메일 |
| password | String | Y | 1~100자 | 로그인 비밀번호 |

- Response DTO: `AccessTokenCarrier`

| 필드 | 타입 | 필수 | 제약 | 설명 |
| --- | --- | --- | --- | --- |
| accessToken | String | Y | - | JWT Access Token |

### GET /api/v1/members/me
- 인증: 필요
- 설명: 내 프로필 조회
- Response DTO: `MemberView`

| 필드 | 타입 | 필수 | 제약 | 설명 |
| --- | --- | --- | --- | --- |
| id | Long | Y | - | 내부 DB ID |
| memberId | UUID | Y | - | 공개 ID |
| email | String | Y | - | 이메일 |
| username | String | Y | - | 사용자명 |
| profileImage | String | N | - | 프로필 이미지 URL |
| phoneNumber | String | N | - | 전화번호 |

## 워크스페이스
### POST /api/v1/workspaces
- 인증: 필요(권장)
- 설명: 워크스페이스 생성
- Request DTO: `CreateWorkspaceDto`

| 필드 | 타입 | 필수 | 제약 | 설명 |
| --- | --- | --- | --- | --- |
| name | String | Y | NotBlank | 워크스페이스 이름 |
| ownerId | UUID | Y | NotNull | 소유자 ID |

- Response: `204 No Content`

### GET /api/v1/workspaces/{workspaceId}
- 인증: 필요
- Response DTO: `WorkspaceView`

| 필드 | 타입 | 필수 | 제약 | 설명 |
| --- | --- | --- | --- | --- |
| workspaceId | UUID | Y | - | 워크스페이스 ID |
| ownerId | UUID | Y | - | 소유자 ID |
| name | String | Y | - | 이름 |

### GET /api/v1/workspaces/by-name/{name}
- 인증: 필요
- Response DTO: `WorkspaceView` (동일)

### GET /api/v1/workspaces/by-owner/{ownerId}
- 인증: 필요
- Response: `WorkspaceView[]`

## 카테고리
### POST /api/v1/categories
- 인증: 필요(권장)
- 설명: 카테고리 생성
- Request DTO: `CreateCategoryDto`

| 필드 | 타입 | 필수 | 제약 | 설명 |
| --- | --- | --- | --- | --- |
| name | String | Y | 최대 100자 | 카테고리 이름 |
| description | String | N | - | 설명 |
| scope | CategoryScope | Y | NotNull | 범위(`PERSONAL`/`WORKSPACE`/`COMMUNITY`) |
| workspaceId | UUID | N | - | 워크스페이스 범위일 때 사용 |
| memberId | UUID | N | - | 개인 범위일 때 사용 |

- Response: `204 No Content`

### GET /api/v1/categories/personal/{categoryId}
### GET /api/v1/categories/workspace/{workspaceId}/{categoryId}
### GET /api/v1/categories/community/{categoryId}
- 인증: 필요
- Response DTO: `CategoryView`

| 필드 | 타입 | 필수 | 제약 | 설명 |
| --- | --- | --- | --- | --- |
| name | String | Y | 최대 100자 | 카테고리 이름 |
| description | String | N | - | 설명 |
| scope | CategoryScope | Y | NotNull | 범위 |
| workspaceId | UUID | N | - | 워크스페이스 ID |
| memberId | UUID | Y | NotNull | 소유자 ID |
| categoryId | UUID | Y | NotNull | 카테고리 ID |

### GET /api/v1/categories/personal
### GET /api/v1/categories/workspace/{workspaceId}
### GET /api/v1/categories/community
- 인증: 필요
- Response: `CategoryView[]`

## 작업
### POST /api/v1/tasks/create
- 인증: 필요
- 설명: 작업 생성
- Request DTO: `CreateTaskDto`

| 필드 | 타입 | 필수 | 제약 | 설명 |
| --- | --- | --- | --- | --- |
| title | String | Y | 최대 255자 | 제목 |
| description | String | N | - | 설명 |
| workspaceId | UUID | Y | NotNull | 워크스페이스 ID |
| categoryId | UUID | N | - | 카테고리 ID |
| ownerId | UUID | Y | NotNull | 담당자 ID |
| templateId | UUID | N | - | 템플릿 카테고리 ID |
| startDate | LocalDate | N | - | 시작일 |
| startTime | LocalTime | N | - | 시작시간 |
| dueDate | LocalDate | N | - | 마감일 |
| dueTime | LocalTime | N | - | 마감시간 |
| priority | Integer | N | 1~5 | 우선순위(기본 3) |
| isComplete | Boolean | N | - | 완료 여부(기본 false) |
| isRoutine | Boolean | N | - | 루틴 여부(기본 false) |
| recurrenceRule | String | N | 최대 255자 | 반복 규칙 |

- Response: `204 No Content`

### PATCH /api/v1/tasks/{taskId}/complete
### PATCH /api/v1/tasks/{taskId}/uncomplete
- 인증: 필요
- Request: 없음
- Response: `204 No Content`

### PUT /api/v1/tasks/{taskId}/title
### PUT /api/v1/tasks/{taskId}
- 인증: 필요
- Request DTO: `UpdateTaskDto`

| 필드 | 타입 | 필수 | 제약 | 설명 |
| --- | --- | --- | --- | --- |
| title | String | Y | 최대 255자 | 제목 |
| description | String | N | - | 설명 |
| startDate | LocalDate | N | - | 시작일 |
| startTime | LocalTime | N | - | 시작시간 |
| dueDate | LocalDate | N | - | 마감일 |
| dueTime | LocalTime | N | - | 마감시간 |
| priority | Integer | N | 1~5 | 우선순위(기본 3) |

- Response: `204 No Content`

### GET /api/v1/tasks/{taskId}
- 인증: 필요
- Response DTO: `TaskView`

| 필드 | 타입 | 필수 | 제약 | 설명 |
| --- | --- | --- | --- | --- |
| taskId | UUID | Y | - | 작업 ID |
| workspaceId | UUID | Y | - | 워크스페이스 ID |
| categoryId | UUID | N | - | 카테고리 ID |
| ownerId | UUID | Y | - | 담당자 ID |
| templateCategoryId | UUID | N | - | 템플릿 카테고리 ID |
| title | String | Y | 최대 255자 | 제목 |
| description | String | N | - | 설명 |
| startDate | LocalDate | N | - | 시작일 |
| startTime | LocalTime | N | - | 시작시간 |
| dueDate | LocalDate | N | - | 마감일 |
| dueTime | LocalTime | N | - | 마감시간 |
| priority | Integer | Y | 1~5 | 우선순위 |
| percentComplete | Integer | Y | 0~100 | 진행률 |
| routine | Boolean | Y | - | 루틴 여부 |
| recurrenceRule | String | N | - | 반복 규칙 |
| status | TaskStatus | Y | - | 상태 |

### GET /api/v1/tasks/member-day?date=YYYY-MM-DD
### GET /api/v1/tasks/member-month?month=YYYY-MM
- 인증: 필요
- Response: `TaskView[]`

## OAuth2 로그인
- `GET /oauth2/authorization/google`
- `GET /oauth2/authorization/github`
- 로그인 성공 후 `/`로 리다이렉트

## 에러 응답(예시)
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/members/issueToken"
}
```

## 참고
- 컨트롤러: `src/main/java/io/app/stacktodobe/**/adapter/in/web/controller/`
- DTO: `src/main/java/io/app/stacktodobe/**/adapter/in/web/dto/`

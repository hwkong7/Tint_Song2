# Tint & Song

화장품(틴트)과 노래를 기록·관리하는 안드로이드 앱입니다.
동일한 기능을 **SwiftUI(iOS)로도 구현**하여, 두 플랫폼의 선언형 UI를 비교하며 개발했습니다.

> 2025 스마트 앱 프로그래밍 (금오공과대학교)

## 기능

- **틴트 목록 조회** — 제품명과 브랜드를 리스트로 표시
- **틴트 상세 조회** — 컬러 칩, 평점, 설명 확인
- **틴트 추가** — 기본 팔레트에서 선택하거나 직접 색상을 지정(HEX 저장)
- **틴트 삭제** — 목록에서 스와이프 삭제
- **노래 목록/상세/추가/삭제** — 제목·가수·평점·가사 관리

## 기술 스택

| 구분 | 사용 기술 |
| --- | --- |
| 언어 | Kotlin |
| UI | Jetpack Compose (Material 3) |
| 아키텍처 | MVVM, 단방향 데이터 흐름(UDF) |
| 상태 관리 | StateFlow, State Hoisting |
| 화면 이동 | Navigation Compose |
| 목록 | LazyColumn |
| 백엔드 | Supabase (PostgREST) |
| 빌드 | Gradle (Kotlin DSL) |

## 구조

```
app/src/main/java/.../
├── model/          # Tint, Song 데이터 클래스
├── data/           # Repository 인터페이스 + Supabase 구현체
├── viewmodel/      # 화면별 ViewModel (StateFlow로 상태 노출)
└── ui/             # Composable 화면 및 공통 컴포넌트
```

**데이터 계층을 인터페이스로 분리**해, 화면 코드를 수정하지 않고 저장소 구현을 교체할 수 있도록 설계했습니다. ViewModel은 Repository를 주입받으며, 화면은 ViewModel이 노출하는 상태만 구독합니다.

## 데이터베이스

```sql
CREATE TABLE public.cosmetics (
  id           uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  product_name text NOT NULL,
  brand        text NOT NULL,
  color_family text,
  color_hex    text,
  rating       integer NOT NULL CHECK (rating >= 1 AND rating <= 10),
  description  text
);
```

## 실행 방법

1. 저장소를 클론한 뒤 Android Studio에서 엽니다.
2. `local.properties`에 Supabase 정보를 추가합니다.

   ```properties
   SUPABASE_URL=https://<project>.supabase.co
   SUPABASE_ANON_KEY=<anon key>
   ```

3. 위 SQL로 테이블을 생성한 뒤 앱을 실행합니다.

## 설계 문서

유스케이스별 시퀀스 다이어그램(목록 조회 / 상세 조회 / 추가 / 삭제)을 작성한 뒤 구현했습니다.

## 회고

- 초기 구현에서는 네트워크 응답을 강제 언래핑으로 처리해 실패 경로가 존재하지 않았습니다. 이후 예외를 상위로 전달하고 화면에서 오류 상태를 표현하도록 수정했습니다.
- 같은 요구사항을 SwiftUI와 Jetpack Compose로 각각 구현하면서, 상태를 끌어올려 화면을 순수하게 유지한다는 원칙은 두 플랫폼에서 동일하게 적용된다는 점을 확인했습니다.

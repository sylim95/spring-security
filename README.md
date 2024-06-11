# Project Summary

이 프로젝트는 Spring-security가 적용된 Spring Boot 프로젝트 입니다. 회원 가입 및 로그인 기능 등을 포함합니다.

## API 문서

이 프로젝트는 Springdoc OpenAPI를 사용하여 API 문서를 자동으로 생성합니다. 로컬에서 프로젝트를 실행한 후, 웹 브라우저에서 다음 URL을 방문하여 API 문서를 확인할 수 있습니다:

* http://localhost:8080/security/swagger.html

## 사용된 도구 요약

* Java 17 - 사용된 프로그래밍 언어
* Spring Boot 3 - 사용된 프레임워크
* Spring Security - 애플리케이션의 보안(인증과 권한, 인가 등)을 담당
* Gradle - 의존성 관리 도구
* Lombok - Java 라이브러리, 코드 단순화 도구
* springdoc-openapi - API 문서화 도구
* H2 - 자바로 작성된 관계형 데이터베이스 관리 시스템
* JPA - Java Persistence API, 데이터 접근 및 관리를 위한 표준
* MapStruct - Java bean 매핑 도구

## 암호화
* Bcrypt 해시 함수 사용

## 토큰 만료 시간
* 1시간

## 기본 응답 포맷
```json
{
    "code": 200,
    "message": "Success",
    "data": { }
}
```
## 에러 응답 포맷
```json
{
    "code": "ERROR" ,
    "message": "",
    "errors": [
        {
            "field": null,
            "value": null,
            "reason": ""
        }
    ]
}
```


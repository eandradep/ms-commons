# ms-commons contributor guide

## Project scope

`ms-commons` is a reusable Spring Boot library for common microservice concerns:
API response wrapping, exception handling, auditing, validation, and shared base
entities. Keep changes library-safe: do not introduce service-specific business
logic or runtime configuration requirements.

## Technology and layout

- Java 17; Spring Boot 3.4.x; Gradle wrapper.
- Production code is under `src/main/java/com/eandrade`:
  - `application/dto`: shared response DTOs.
  - `domain/entity` and `domain/exception`: reusable domain primitives.
  - `infrastructure/config`, `infrastructure/handler`, and `infrastructure/audit`:
    Spring integration and cross-cutting support.
- Configuration defaults belong in `src/main/resources/application.properties`.

## Build and verification

Run from the repository root:

```bash
./gradlew test
./gradlew build
```

Use the Gradle wrapper; do not depend on a locally installed Gradle version.
Add focused JUnit tests under `src/test/java` for behavior changes, especially
for HTTP response and exception-mapping behavior.

## Implementation conventions

- Use the `com.eandrade` package hierarchy and keep layer boundaries intact.
- Prefer constructor injection and explicit Spring beans/configuration.
- Preserve the existing `ApiResponse` shape and Spanish user-facing error
  messages when extending `GlobalExceptionHandler`.
- Keep public library APIs backward compatible unless the change explicitly
  warrants a breaking release.
- Use Jakarta (`jakarta.*`) APIs; this project targets Spring Boot 3.
- Lombok is compile-only: do not make consumers rely on generated code without
  ensuring the public API remains usable.

## Publishing

The normal JAR (not a Spring Boot executable JAR) is published to GitHub
Packages. `build.gradle` intentionally disables `bootJar`, configures the
library artifact as `ms-commons`, and publishes sources. Preserve those settings
when modifying dependencies or publication metadata. Version changes must be
made in `build.gradle` and should be deliberate because they affect consumers.

## Change hygiene

- Avoid unrelated formatting or generated-file changes.
- Do not commit credentials; publishing credentials are supplied by Gradle
  properties or `GITHUB_ACTOR` / `GITHUB_TOKEN` environment variables.
- Review the GitHub Actions workflows when changing build, quality, or
  publishing behavior.

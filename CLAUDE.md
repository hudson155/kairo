# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Git Workflow

Never push directly to main. Always create a pull request for changes.

## Build Commands

```bash
# Build all modules
./gradlew build

# Build a specific module
./gradlew :kairo-rest:build

# Run all tests
./gradlew test

# Run tests for a specific module
./gradlew :kairo-rest:test

# Run a single test class
./gradlew :kairo-rest:test --tests "kairo.rest.SomeTest"

# Run detekt (linting/static analysis)
./gradlew detektMain detektTest

# Check (build + detekt + tests)
./gradlew check
```

Detekt auto-corrects formatting issues when `CI != true`. Warnings are treated as errors.

## Architecture Overview

Kairo is a modular toolkit for Kotlin backends with two library categories:

**Standalone Libraries** - No interdependencies, usable anywhere:
- `kairo-config`: HOCON configuration with env var substitution and secret resolvers
- `kairo-serialization`: Jackson wrapper for JSON serialization
- `kairo-id`: Human-readable semantic identifiers
- `kairo-logging`: SLF4J with kotlin-logging interface
- `kairo-exception`: Logical failures vs real exceptions
- `kairo-reflect`: Unified `KairoType<T>` reflection abstraction
- `kairo-testing`: JUnit 5 + Kotest + MockK integrated testing

**Application Libraries** - For full Kairo applications, built on the Feature system:
- `kairo-feature`: Core Feature base class with lifecycle handlers
- `kairo-server`: Composes Features into a Server
- `kairo-rest`: Ktor-based REST with type-safe endpoint DSL
- `kairo-sql`: Exposed ORM with R2DBC async support
- `kairo-dependency-injection`: Koin-based DI with KSP code generation

### Feature System Pattern

Applications are composed of Features (both framework and domain):

```kotlin
class MyFeature : Feature() {
  override val name: String = "MyFeature"
  override val lifecycle: List<LifecycleHandler> = lifecycle { /* handlers */ }
}

val server = Server(
  features = listOf(
    DependencyInjectionFeature(koinApplication),
    RestFeature(config.rest),
    MyDomainFeature(koin),
  ),
)
```

### REST Endpoint Pattern

Type-safe endpoints with annotation-based routing:

```kotlin
object UserApi {
  @Rest("GET", "/users/:userId")
  data class Get(
    @PathParam val userId: UserId,
  ) : RestEndpoint<Unit, UserRep>()
}

// Handler in routing:
route(UserApi.Get::class) {
  handle { userService.get(endpoint.userId) }
}
```

### SQL Access Pattern

Exposed DSL with suspend transactions:

```kotlin
suspend fun get(id: UserId) =
  suspendTransaction(db = database) {
    UserTable.selectAll()
      .where { UserTable.id eq id }
      .map(UserModel::fromRow)
      .singleNullOrThrow()
  }
```

## Code Style

- Java 21, Kotlin with `allWarningsAsErrors = true` and `explicitApi()`
- 2-space indentation, 120 character line length
- Trailing commas required
- camelCase for constants (not SCREAMING_SNAKE_CASE)
- Singular naming for packages, folders, Features (REST paths are plural)
- Full sentences with punctuation in comments
- Order members by read-create-update-delete pattern
- Use "non-null" not "not null"

## Testing

Tests use `kairo-testing` (JUnit 5 + Kotest matchers + MockK) with `runTest` for coroutines:

```kotlin
@Test
fun `Test name with backticks`(): Unit = runTest {
  // test code
}
```

Integration tests use `kairo-integration-testing` with `setup`, `test`, `postcondition` blocks.

## Module Structure

Each module follows: `src/main/kotlin/kairo/<module>/` with tests in `src/test/kotlin/`.

Some modules have submodules (e.g., `kairo-rest/feature/`, `kairo-rest/endpoint/`).

All packages start with `kairo.` (enforced by detekt's `InvalidPackageDeclaration`).

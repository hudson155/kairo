# Postgres integration testing

Library to help with Kairo integration tests for Features that use Postgres.
Read the general [integration testing README](..) first.

Uses testcontainers to run Postgres in Docker,
creating a separate database for each test.
This approach preserves the ability to run tests in parallel.

## Installation

Install `kairo-integration-testing-postgres`.

```kotlin
// build.gradle.kts

dependencies {
  testImplementation("software.airborne.kairo:kairo-integration-testing")
  testImplementation("software.airborne.kairo:kairo-integration-testing-postgres")
}
```

## Usage

Make your Feature's JUnit extension extend aware of the Postgres extension,
and initialize the database in `beforeEach`.

```kotlin
class UserFeatureTest : FeatureTest(), PostgresExtensionAware {
  override fun beforeEach(context: ExtensionContext) {
    transaction(db = checkNotNull(context.database)) {
      SchemaUtils.createSchema(Schema("user"))
      SchemaUtils.create(
        UserTable,
      )
    }
    super.beforeEach(context)
  }

  override fun createServer(context: ExtensionContext, koinApplication: KoinApplication): Server =
    Server(
      name = "User Feature Test Server",
      features = listOf(
        DependencyInjectionFeature(koinApplication),
        UserFeature(koinApplication.koin),
        SqlFeature.from(checkNotNull(context.connectionFactory)),
      ),
    )
}
```

Now simply add `@ExtendWith(PostgresExtension::class)` to your test classes.

```kotlin
@ExtendWith(PostgresExtension::class, UserFeatureTest::class)
internal class GetUserTest {
  // ...
}
```

```kotlin
@ExtendWith(PostgresExtension::class, UserFeatureTest::class)
internal class CreateUserTest {
  // ...
}
```

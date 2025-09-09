# Kairo Features

Features are the **core building block** of Kairo applications.
Each Feature defines an encapsulated piece of functionality.

There are 2 types of Features.

1. **Framework Features:**
   Support things like REST, SQL, various GCP services, etc.
   Many of these are defined by Kairo, but you can create your own too.
2. **Domain Features:**
   These make up your application code,
   each representing a concern like users, businesses, billing, etc.

### Why Features?

- **Encapsulation:**
  Each Feature owns its lifecycle (`start()` / `end()`)
  and configuration.
  **Clear boundaries** mean more maintainable code.
- **Composability:**
  Servers are composed by combining Features.
  **No hidden assumptions**
  means you're not tied to patterns that don't make sense for your application.

## Installation

Install `kairo-feature`.\
But if you've already installed `kairo-server` or `kairo-application`,
you don't need `kairo-feature` too.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-feature")
}
```

## Usage

### Framework Features

The following Framework Features are currently available.

- [DependencyInjectionFeature](../kairo-dependency-injection/feature):
  Uses Koin for reflection-free, Kotlin-friendly dependency injection.
- [HealthCheckFeature](../kairo-health-check/feature):
  Adds configurable health check REST endpoints to your application.
- [RestFeature](../kairo-rest/feature):
  Declarative REST endpoints wired through Ktor.
- [SqlFeature](../kairo-sql/feature):
  Type-safe SQL with async I/O.

You can also create your own —
most of these are fairly straightforward.

### Creating a Domain Feature

These make up your application code,
each representing a concern like users, businesses, billing, etc.

```kotlin
class UserFeature(
  private val koin: Koin,
) : Feature(), HasRouting {
  override val name: String = "User"

  private val userHandler: UserHandler get() = koin.get()

  override fun Application.routing() {
    routing {
      with(userHandler) { routing() }
    }
  }
}
```

#### Lifecycle events

Features can hook into Server lifecycle events by implementing the `start()` and `stop()` methods.
These methods run in parallel with other Features,
so they must be thread/coroutine-safe.

More advanced Features may also implement the other lifecycle hooks
(see `LifecycleEventListener`),
but this should be done sparingly.

### Putting it all together

For a full guide, see the [Kairo Application Guide](../docs/application-guide.md).

```kotlin
val koinApplication = koinApplication()

val healthChecks = mapOf(
  "sql" to HealthCheck { SqlFeature.healthCheck(koinApplication.koin.get()) },
)

val features = listOf(
  // Framework Features.
  DependencyInjectionFeature(koinApplication),
  HealthCheckFeature(healthChecks),
  RestFeature(config.rest),
  SqlFeature(
    config = config.sql,
    configureDatabase = {
      explicitDialect = PostgreSQLDialect()
    },
  ),

  // Domain Feature.
  UserFeature(koinApplication.koin),
)

val server = Server(
  name = "...",
  features = features,
)
```

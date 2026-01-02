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
  Each Feature owns its lifecycle and configuration.
  **Clear boundaries** mean more maintainable code.
- **Composability:**
  Servers are composed of Features.
  **No hidden assumptions**
  means you're not tied to patterns that don't make sense for your application.

## Installation

Install `kairo-feature`.
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

- [ClientFeature](../kairo-client/README.md):
  Ktor-native outgoing HTTP requests from your Kairo application.
- [DependencyInjectionFeature](../kairo-dependency-injection/README.md):
  Uses Koin for reflection-free, Kotlin-friendly dependency injection.
- [HealthCheckFeature](../kairo-health-check/README.md):
  Adds configurable health check REST endpoints to your application.
- [RestFeature](../kairo-rest/README.md):
  Declarative REST endpoints wired through Ktor.
- [SqlFeature](../kairo-sql/README.md):
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

  @Routing
  override fun Application.routing() {
    routing {
      with(userHandler) { routing() }
    }
  }
}
```

### Lifecycle handlers

Features can hook into Server lifecycle events by implementing lifecycle handlers.
Lifecycle handlers define a priority (most Features should use the default priority).
All handlers with the same priority run in parallel,
so they must be thread/coroutine-safe.

```kotlin
class UserFeature(
  private val koin: Koin,
) : Feature(), HasRouting {
  override val name: String = "User"

  private val userHandler: UserHandler get() = koin.get()

  override val lifecycle: List<LifecycleHandler> =
    lifecycle {
      handler {
        start {
          // ...
        }
        stop {
          // ...
        }
      }
    }

  @Routing
  override fun Application.routing() {
    routing {
      with(userHandler) { routing() }
    }
  }
}
```

### Putting it all together

Combine your Features into a [Server](../kairo-server/README.md).

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

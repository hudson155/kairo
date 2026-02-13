# Kairo Server

Servers are composed of [Features](../kairo-feature/README.md),
and can be run as an application or as part of integration tests.

## Installation

Install `kairo-server`.
But if you've already installed `kairo-application`,
you don't need `kairo-server` too.

```kotlin
// build.gradle.kts

dependencies {
  implementation("com.highbeam.kairo:kairo-server")
}
```

## Usage

All you need are your [Features](../kairo-feature/README.md) and a name.

```kotlin
val features = listOf(
  // ...
)

val server = Server(
  name = "...",
  features = features,
)
```

You can run the Server as an application using [kairo-application](../kairo-application/README.md),
or use it in [integration tests](../kairo-integration-testing/README.md).

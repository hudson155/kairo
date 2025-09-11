# Kairo Server

Servers are composed by [Features](../kairo-feature),
and can be run as an application or as part of integration tests.

### Sample usage

Sample usage from the [kairo-sample](https://github.com/hudson155/kairo-sample) repo.

- [Main.kt](https://github.com/hudson155/kairo-sample/blob/main/src/main/kotlin/kairoSample/Main.kt)

## Installation

Install `kairo-server`.\
But if you've already installed `kairo-application`,
you don't need `kairo-server` too.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-server")
}
```

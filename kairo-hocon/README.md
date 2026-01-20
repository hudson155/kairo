# HOCON

Wrapper around [lightbend/config](https://github.com/lightbend/config)
along with JSON serialization support.

The primary use case is in service of [kairo-config](../kairo-config/README.md).

## Installation

Install `kairo-hocon`.
You don't need to install lightbend/config separately —
it's included by default.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-hocon")
}
```

## Usage

```kotlin
data class Config(
  val rest: RestFeatureConfig,
  val sql: SqlFeatureConfig,
)

val json: KairoJson = KairoJson()

val hocon = ConfigFactory.parseResources("resource.conf").resolve()

json.deserialize<Config>(hocon)
```

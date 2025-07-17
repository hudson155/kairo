# `kairo-clock-feature`

The Clock Feature allows for configuration and injection of a Java `Clock`.
In production this will more than likely be a real UTC-based clock,
but for tests it may be a fixed clock instead.

## Installation

`software.airborne.kairo:kairo-clock-feature:5.14.0`

<details>

<summary>Gradle</summary>

```kotlin
plugins {
  id("com.google.cloud.artifactregistry.gradle-plugin")
}

repositories {
  maven {
    url = uri("artifactregistry://us-central1-maven.pkg.dev/airborne-software/maven")
  }
}

dependencies {
  implementation("software.airborne.kairo:kairo-clock-feature:5.14.0")
}
```

</details>

## Usage

### Configure and instantiate the Feature

```yaml
clock:
  type: "System"
  timeZone: "UTC"
```

```kotlin
KairoClockFeature(config.clock)
```

### Inject and use the Clock

```kotlin
val clock = injector.getInstance<Clock>()
val now = Instant.now(clock)
```

# `kairo-clock-feature`

The Clock Feature allows for configuration and injection of a Java `Clock`.
In production this will more than likely be a real UTC-based clock,
but for tests it may be a fixed clock instead.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  testImplementation("kairo:kairo-clock-feature:0.4.0")
}
```

### Step 2: Create and configure the Feature

```yaml
# src/main/resources/config/config.yaml

clock:
  type: "System"
  timeZone: "UTC"
```

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

KairoClockFeature(config.clock)
```

### Step 3: Inject and use the Clock

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

class MyClass @Inject constructor(
  private val clock: Clock,
) {
  fun myMethod() {
    Instant.now(clock)
  }
}
```

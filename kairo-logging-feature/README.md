# `kairo-logging-feature`

See [kairo-logging](/kairo-logging/) for the underlying logging implementation.

This Feature supports logging within Kairo Servers.
After installing this Feature you can disable Log4j2's shutdown hook
and let this Feature handle shutdown.
Doing this ensures that logging is not flushed and disabled until the other Features shut down.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-logging-feature:0.4.0")
}
```

### Step 2: Create and configure the Feature

```yaml
# src/main/resources/config/config.yaml

logging:
  shutDownManually: true
```

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

KairoLoggingFeature(config.logging)
```

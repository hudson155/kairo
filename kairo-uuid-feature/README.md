# `kairo-uuid-feature`

Includes testable UUID generation code.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-uuid-feature:$kairoVersion")
}
```

### Step 2: Create and configure the Feature

```yaml
# src/main/resources/config/config.yaml

uuid:
  generator:
    type: "Random"
```

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

KairoUuidFeature(config.uuid)
```

### Step 3: Generate UUIDs

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

class MyClass @Inject constructor(
  private val uuidGenerator: UuidGenerator,
) {
  fun myMethod() {
    uuidGenerator.generate()
  }
}
```

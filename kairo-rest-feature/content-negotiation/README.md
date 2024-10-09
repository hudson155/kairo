# `kairo-rest-feature:cors`

The content negotiation extension to the REST Feature adds support for JSON serialization and deserialization.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-rest-feature-cors:$kairoVersion")
}
```

### Step 2: Configure

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

KairoRestFeature(config.rest) {
  install(ContentNegotiation) {
    kairoConfigure()
  }
}
```

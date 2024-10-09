# `kairo-rest-feature:cors`

The CORS extension to the REST Feature adds support for cross-origin resource sharing (CORS).

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-rest-feature-cors:$kairoVersion")
}
```

### Step 2: Configure

```yaml
# src/main/resources/config/config.yaml

cors:
  hosts:
    - { host: "localhost:3000", scheme: "http" }
  headers:
    - "Authorization"
    - "Content-Type"
```

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

KairoRestFeature(config.rest) {
  install(CORS) {
    kairoConfigure(config.cors)
  }
}
```

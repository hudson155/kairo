# `kairo-google-app-engine-feature`

Kairo's Google App Engine Feature uses the health check readiness endpoint
to configure a GAE warmup endpoint: `GET /_ah/warmup`.
[kairo-health-check-feature](/kairo-health-check-feature) must also be installed.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-google-app-engine-feature:$kairoVersion")
}
```

### Step 2: Create the Feature

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

KairoGoogleAppEngineFeature()
```

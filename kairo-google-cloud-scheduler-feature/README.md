# `kairo-google-cloud-scheduler-feature`

Kairo's Google Cloud Scheduler Feature enables CRON
using [Google Cloud Scheduler](https://cloud.google.com/scheduler/docs).

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-google-cloud-scheduler-feature:$kairoVersion")
}
```

### Step 3: Create a job

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

class MyClass @Inject constructor(
  private val googleCloudScheduler: GoogleCloudScheduler,
) {
  fun myMethod(libraryBookId: KairoId) {
    googleCloudScheduler.create(
      endpoint = LibraryBookApi.Read(libraryBookId),
      config = GoogleCloudScheduler.Config(...),
    )
    googleCloudScheduler.delete(...)
  }
}
```

### Step 4: Create and configure the Feature

```yaml
# src/main/resources/config/config.yaml

googleCloudScheduler:
  type: "Http"
  baseUrl: "https://example.com/"
  projectId: "secret-grammar-201908"
  location: "us-central1"
  jobName: { }
```

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

KairoGoogleCloudSchedulerFeature(config.googleCloudScheduler)
```

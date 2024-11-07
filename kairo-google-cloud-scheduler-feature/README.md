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
  private val jobCreator: JobCreator,
) {
  fun myMethod(libraryBookId: KairoId) {
    jobCreator.create(
      endpoint = LibraryBookApi.Read(libraryBookId),
      config = JobCreator.Config(...),
    )
  }
}
```

### Step 4: Create and configure the Feature

```yaml
# src/main/resources/config/config.yaml

googleCloudScheduler:
  type: "Real"
  projectId: "secret-grammar-201908"
  location: "us-central1"
  jobName: { }
```

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

KairoGoogleCloudSchedulerFeature(config.googleCloudScheduler)
```

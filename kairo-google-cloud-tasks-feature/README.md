# `kairo-google-cloud-tasks-feature`

Kairo's Google Cloud Tasks Feature enables task management
using [Google Cloud Tasks](https://cloud.google.com/tasks/docs).

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-google-cloud-tasks-feature:$kairoVersion")
}
```

### Step 2: Annotate your endpoint

Add the `@GoogleCloudTasks.Queue` annotation to your endpoint
to specify which GCP queue it should go on

```kotlin
// src/main/kotlin/yourPackage/entity/libraryBook/LibraryBookActionApi.kt

object LibraryBookApi {
  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library-books/:libraryBookId/read")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  @GoogleCloudTasks.Queue("integration-center")
  data class Read(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, Unit>()
}
```

### Step 3: Create a task

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

class MyClass @Inject constructor(
  private val googleCloudTasks: GoogleCloudTasks,
) {
  fun myMethod(libraryBookId: KairoId) {
    googleCloudTasks.create(LibraryBookApi.Read(libraryBookId))
  }
}
```

### Step 4: Create and configure the Feature

```yaml
# src/main/resources/config/config.yaml

googleCloudTasks:
  type: "TransactionAware"
  delegate:
    type: "Real"
    projectId: "secret-grammar-201908"
    location: "us-central1"
    queueName: { }
```

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

KairoGoogleCloudTasksFeature(config.googleCloudTasks)
```

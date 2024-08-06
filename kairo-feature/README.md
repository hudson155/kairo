# `kairo-feature`

Features are the primary building block of Kairo applications.
Every piece of functionality,
from framework-level REST or SQL behaviour
to application-specific implementations
to third-party integrations
is represented by a Kairo Feature.

Note: Treat Kairo _Features_ as proper nouns (the first letter should be capitalized).

## Usage

### Step 1: Include the dependency

```kotlin
dependencies {
  api("kairo:kairo-feature:0.3.0")
}
```

### Step 2: Create your first Feature

This is an example `TaskFeature` for managing a to-do list.
`yourPackage` represents whatever top-level package you're using for your project.

```kotlin
// src/main/kotlin/yourPackage/feature/task/TaskFeature.kt

class TaskFeature : Feature() {
  override val name: String = "Task"

  // Use "Normal" for most Features.
  override val priority: FeaturePriority = FeaturePriority.Normal
}
```

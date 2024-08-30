# `kairo-feature`

Features are Kairo's main building block.
Every piece of functionality,
from framework-level REST or SQL behaviour
to application-specific implementations
to third-party integrations
is represented by a Kairo Feature.

Note: Treat Kairo _Features_ as proper nouns (the first letter should be capitalized).

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  api("kairo:kairo-feature:$kairoVersion")
}
```

### Step 2: Create your first Feature

This is an example `LibraryFeature` for managing a public library.
`yourPackage` represents whatever top-level package you're using for your project.

```kotlin
// src/main/kotlin/yourPackage/feature/library/LibraryFeature.kt

class LibraryFeature : Feature() {
  override val name: String = "Library"

  // Use "Normal" for most Features.
  override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: Binder) {
    // Do your Guice binding here!
  }
}
```

# `kairo-closeable`

`KairoCloseable` is similar to Java's `Closeable`, but it supports coroutines.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-closeable:$kairoVersion")
}
```

### Step 2: Implement and use a `KairoCloseable`

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

class MyCloseable : KairoCloseable {
  override fun close() {
    // ...
  }
}

MyCloseable().use { myCloseable ->
  // ...
}
```

### Step 3: Implement and use a `KairoCloseable.Suspend`

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

class MyCloseable : KairoCloseable.Suspend {
  override suspend fun close() {
    // ...
  }
}

MyCloseable().use { myCloseable ->
  // ...
}
```

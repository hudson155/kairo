# `kairo-closeable`

`KairoCloseable` is similar to Java's `Closeable`, but it supports coroutines.

## Installation

`software.airborne.kairo:kairo-closeable:5.13.0`

<details>

<summary>Gradle</summary>

```kotlin
plugins {
  id("com.google.cloud.artifactregistry.gradle-plugin")
}

repositories {
  maven {
    url = uri("artifactregistry://us-central1-maven.pkg.dev/airborne-software/maven")
  }
}

dependencies {
  implementation("software.airborne.kairo:kairo-closeable:5.13.0")
}
```

</details>

## Usage

`KairoCloseable` does not support suspending closures.

```kotlin
class MyCloseable : KairoCloseable {
  override fun close() {
    // ...
  }
}

MyCloseable().use { myCloseable ->
  // ...
}
```

`KairoCloseable.Suspend` does support suspending closures.

```kotlin
class MyCloseable : KairoCloseable.Suspend {
  override suspend fun close() {
    // ...
  }
}

MyCloseable().use { myCloseable ->
  // ...
}
```

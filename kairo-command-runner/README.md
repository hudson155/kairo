# `kairo-comand-runner`

`CommandRunner` runs shell commands.
It delegates to Java's built-in way of doing this,
but uses an abstract class for testability.

This is not considered secure, and should not be used in production unless additional measures are in place.

## Installation

`software.airborne.kairo:kairo-command-runner:$kairoVersion`

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
  implementation("software.airborne.kairokairo-command-runner:$kairoVersion")
}
```

</details>

## Usage

```kotlin
val commandRunner: CommandRunner = DefaultCommandRunner
@OptIn(CommandRunner.Insecure::class)
commandRunner.run("echo \"Hello, World!\"") // Hello, World!
```

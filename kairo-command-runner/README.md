# `kairo-comand-runner`

`CommandRunner` runs shell commands.
It delegates to Java's built-in way of doing this,
but uses an abstract class for testability.

This is not considered secure, and should not be used in production unless additional measures are in place.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  api("kairo:kairo-command-runner:$kairoVersion")
}
```

### Step 2: Run a command

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

val commandRunner: CommandRunner = DefaultCommandRunner
commandRunner.run("echo \"Hello, World!\"") // Hello, World!
```

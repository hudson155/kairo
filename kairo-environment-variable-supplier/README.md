# `kairo-environment-variable-supplier`

**Refer to the [kairo-sample](https://github.com/hudson155/kairo-sample) repository to see it in action.**

`EnvironmentVariableSupplier` supplies environment variables.
It delegates to Java's built-in way of doing this,
but uses an abstract class for testability.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  api("kairo:kairo-environment-variable-supplier:$kairoVersion")
}
```

### Step 2: Get an environment variable

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

val environmentVariableSupplier: EnvironmentVariableSupplier = DefaultEnvironmentVariableSupplier
environmentVariableSupplier["MESSAGE", "Default value."]
```

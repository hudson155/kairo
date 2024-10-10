# `kairo-mdc`

Adds support for mapped diagnostic context (MDC),
which is the JVM's way of adding context to log lines.
To make MDC work properly with Kotlin coroutines,
use the `withMdc` method from this library.

Note that null MDC values will be excluded.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-mdc:$kairoVersion")
}
```

### Step 2: Use MDC

```kotlin
val mdc = mapOf("key" to "value")
withMdc(mdc) {
  ...
}
```

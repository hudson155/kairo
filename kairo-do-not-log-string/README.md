# `kairo-do-not-log-string`

Library for the `DoNotLogString` type.

`DoNotLogString` represents a string value that should not be logged in its entirety
because it's very long.
Similar to `ProtectedString` but with a different purpose.

Although `toString()` calls will not expose the underlying value,
serialization (and deserialization) will treat this class as if it were just a `String`,
thereby exposing the underlying value.
This is intentional.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-do-not-log-string:$kairoVersion")
}
```

### Step 2: Use do-not-log strings

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

val essay = DoNotLogString("George Orwell, Why I Write (1946)")

// String conversion.
essay.toString() // DoNotLogString(hash='c4ca4238a0b923820dcc509a6f75849b', length=1, truncated='1').

// Serialization and deserialization.
val mapper: JsonMapper = TODO()
mapper.writeValueAsString(essay) // George Orwell, Why I Write (1946).
mapper.readValue<DoNotLogString>("George Orwell, Why I Write (1946)")

// Direct value access.
essay.value
```

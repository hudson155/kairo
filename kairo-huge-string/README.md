# `kairo-huge-string`

Library for the `HugeString` type.

`HugeString` represents a string value that should not be logged or otherwise exposed
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
  implementation("kairo:kairo-huge-string:$kairoVersion")
}
```

### Step 2: Use huge strings

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

val essay = HugeString("George Orwell, Why I Write (1946)")

// String conversion.
essay.toString() // HugeString(hash='c4ca4238a0b923820dcc509a6f75849b', length=1, truncated='1').

// Serialization and deserialization.
val mapper: JsonMapper = TODO()
mapper.writeValueAsString(essay) // George Orwell, Why I Write (1946).
mapper.readValue<HugeString>("George Orwell, Why I Write (1946)")

// Direct value access.
essay.value
```

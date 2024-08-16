# `kairo-protected-string`

Library for the `ProtectedString` type.

`ProtectedString` represents a string value that should not be logged or otherwise exposed, such as an API key.
Note that this is a simplistic implementation; it does not clean up memory after itself.
See the class itself for KDoc.

Although `toString()` calls will not expose the value,
serialization (and deserialization) will treat this class as if it were just a `String`,
thereby exposing the value.
This is intentional.

Other than for serialization, you will need to 

## Usage

### Step 1: Include the dependency

```kotlin
dependencies {
  testImplementation("kairo:kairo-protected-string:0.3.0")
}
```

### Step 2: Use protected strings

```kotlin
// src/main/kotlin/yourPackage/YourFile.kt

val apiKey = ProtectedString("YOUR_API_KEY")

// String conversion.
apiKey.toString() // REDACTED.

// Serialization and deserialization.
mapper.writeValueAsString(apiKey) // YOUR_API_KEY (not redacted).
mapper.readValue<ProtectedString>("YOUR_API_KEY")

// Direct value access.
@OptIn(ProtectedString.Access::class)
apiKey.value
```

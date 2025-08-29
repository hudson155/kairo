# Protected Strings

Secrets should never show up in logs or stack traces.\
`kairo-protected-string` provides a **lightweight wrapper around sensitive strings**
that improves safety without complicating your code.

- **Safe by default:**
  Calling `toString()` yields `REDACTED` instead of the value.
- **Serialization-compatible:**
  Serialization _will_ expose the underlying value.
- **Explicit access:**
  To access the raw value you must opt in using
  `@OptIn(ProtectedString.Access::class)`.
- **Minimal friction:**
  Pass protected strings around normally;
  only creation and access require opting in.

Kairo's protected strings strike a balance between
**developer ergonomics** and **operational safety**.

_Note that this is not cryptographic memory protection.
Protected strings **do not clean up memory after themselves**.
Use them to prevent accidental leaks, not to secure against memory inspection._

### Example

```kotlin
val apiKey = ProtectedString("your_api_key")

println(apiKey)
// => ProtectedString(value='REDACTED')
```

## Installation

Install `kairo-protected-string`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-protected-string")
}
```

## Usage

You must explicitly opt in to create protected strings or access their values.

```kotlin
@OptIn(ProtectedString.Access::class)
```

Once you've opted in, usage is simple.

```kotlin
val apiKey = ProtectedString("your_api_key")

println(apiKey)
// => ProtectedString(value='REDACTED')

println(apiKey.value)
// => your_api_key

println(Json.encodeToString(apiKey))
// => "your_api_key"
```

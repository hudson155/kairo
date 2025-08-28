# Protected Strings

Protected strings are designed to represent string values
that should not be logged or otherwise exposed, such as API keys.

- The `toString` implementation redacts the value.
- Accessing `value` is allowed only by opting into `ProtectedString.Access`.
- Serialization _will_ expose the underlying value.

Note that this is a simplistic implementation; **it does not clean up memory after itself**.

```kotlin
val apiKey = ProtectedString("your_api_key")

println(apiKey)
// => ProtectedString(value='REDACTED')
```

## Installation

Get started by installing `kairo-protected-string`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-protected-string")
}
```

## Usage

Opting into `ProtectedString.Access` is necessary
to create instances of `ProtectedString` or to access their underlying values.
However, this annotation should only be used where protected strings are **initially created**
or where their **underlying values are accessed**.
It isn't necessary (and in fact should be avoided)
to use this annotation to pass protected strings around within your code.

```kotlin
@OptIn(ProtectedString.Access::class)
```

Once you've opted in, simply call the constructor or access the `value` property.

```kotlin
val apiKey = ProtectedString("your_api_key")

println(apiKey)
// => ProtectedString(value='REDACTED')

println(apiKey.value)
// => your_api_key

println(Json.encodeToString(apiKey))
// => "your_api_key"
```

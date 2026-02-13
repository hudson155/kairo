# Optionals

Differentiate between missing and null values.

[RFC 7396 (JSON Merge Patch)](https://datatracker.ietf.org/doc/html/rfc7396)
specifies:

- Missing properties in JSON PATCH requests
  should be interpreted as "preserve the existing value".
- `null` properties in JSON PATCH requests
  should be interpreted as "remove this property".

In order to implement this, we must have some way to differentiate between missing and null properties.
Traditional serialization libraries like `kotlinx.serialization` and Jackson don't provide this out of the box.
Kairo provides `Optional<T>`.

```kotlin
json.deserialize<Optional>("{}")
// => Optional.Missing

json.deserialize<Optional>("""{"value":null}""")
// => Optional.Null

json.deserialize<Optional>("""{"value":"some value"}""")
// => Optional.Value("some value")
```

## Installation

Install `kairo-optional`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("com.highbeam.kairo:kairo-optional")
}
```

## Usage

There are two important points to note when using `Optional<T>`.

First, you must add the `optionalModule` to your `KairoJson` instance.

```kotlin
val json: KairoJson =
  KairoJson {
    addModule(OptionalModule())
  }
```

Second, you must add the `@JsonInclude(JsonInclude.Include.NON_ABSENT)` annotation
to the class or property.

```kotlin
@JsonInclude(JsonInclude.Include.NON_ABSENT)
data class Update(
  val value: Optional<String> = Optional.Missing,
)
```

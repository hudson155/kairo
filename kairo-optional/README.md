# Kairo Optionals

Differentiate between missing and null values.

[RFC 7396 (JSON Merge Patch)](https://datatracker.ietf.org/doc/html/rfc7396)
specifies:

- Missing properties in JSON PATCH requests
  should be interpreted as "preserve the existing value".
- `null` properties in JSON PATCH requests
  should be interpreted as "remove this property".

In order to implement this, we must have some way to differentiate between missing and null properties,
which traditional serialization libraries like `kotlinx.serialization` and Jackson don't provide this out of the box.
Kairo provides `Optional<T>`.

```kotlin
json.decodeFromString<Optional>("{}")
// => Optional.Missing

json.decodeFromString<Optional>("""{"value":null}""")
// => Optional.Null

json.decodeFromString<Optional>("""{"value":"some value"}""")
// => Optional.Value("some value")
```

## Installation

Install `kairo-optional`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-optional")
}
```

## Usage

There are two important points to note when using `Optional<T>`.

First, you must either use `kairo()` to configure your `Json` instance,
or manually add the `optionalModule`.

```kotlin
val json: Json =
  Json {
    kairo()
  }

// or

val json: Json =
  Json {
    serializersModule = SerializersModule {
      include(optionalModule)
    }
  }
```

Second, you must add the `@EncodeDefault(EncodeDefault.Mode.NEVER)` and `@Contextual` annotations
to your `Optional` properties.
You must also specify `Optional.Missing` as the default value.

```kotlin
@Serializable
data class Update(
  @EncodeDefault(EncodeDefault.Mode.NEVER) @Contextual
  val value: Optional<String> = Optional.Missing,
)
```

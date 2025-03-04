# `kairo-serialization`

Uses [Jackson](https://github.com/FasterXML/jackson)
to handle JSON, YAML, and even XML serialization in many contexts,
including Kairo Server configs, REST request and response bodies, and more.

We don't use [kotlinx-serialization](https://github.com/Kotlin/kotlinx.serialization)
(although it is an excellent library)
because of its limited support for polymorphism.

### Type support

- Primitive-like
  - `Boolean`
  - `Double`
  - `Float`
  - `Int`
  - `Long`
  - `String`
- Java
  - `Money` (see [kairo-alternative-money-formatters](../kairo-alternative-money-formatters))
  - `Optional`
- Kotlin
  - `Uuid`
- Ktor
  - `ContentType`
  - `HttpMethod`
- Date/time
  - `Instant`
  - `LocalDate`
  - `Year`
  - `YearMonth`
  - `ZoneId`

### Additional functionality

Some additional functionality is supported for string deserialization.

- The `TrimWhitespace` annotation removes whitespace from the start and/or end of strings during deserialization.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-serialization:$kairoVersion")
}
```

### Step 2: Create and use the object mapper

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

val mapper: JsonMapper = jsonMapper()
// Change ObjectMapperFormat.Json to use other formats, such as YAML.

// Serialize.
val myClass: MyClass = TODO()
mapper.kairoWrite(myClass)

// Deserialize.
val myString: String = TODO()
mapper.readValue<MyClass>(myString) // Be sure to import readValue.
```

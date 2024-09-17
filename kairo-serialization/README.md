# `kairo-serialization`

Uses [Jackson](https://github.com/FasterXML/jackson)
to handle JSON and YAML serialization in many contexts,
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
  - `Optional`
- Kotlin
  - `Uuid`
- Date/time
  - `Instant`
  - `LocalDate`
  - `ZoneId`

### Additional functionality

Some additional functionality is supported for string deserialization.

- The [TrimWhitespace] annotation removes whitespace from the start and/or end of strings during deserialization.
- The [TransformCase] annotation transforms the case (lowercase/uppercase) during deserialization.

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

val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()
// Change ObjectMapperFormat.Json to use other formats, such as YAML.

// Serialize.
val myClass: MyClass = TODO()
mapper.writeValueAsString(myClass)

// Deserialize.
val myString: String = TODO()
mapper.readValue<MyClass>(myString) // Be sure to import readValue.
```

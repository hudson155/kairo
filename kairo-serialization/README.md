# `kairo-serialization`

Uses [Jackson](https://github.com/FasterXML/jackson)
to handle JSON and YAML serialization in many contexts,
including Kairo server configs, REST request and response bodies, and more.

## Usage

### Step 1: Include the dependency

```kotlin
dependencies {
  testImplementation("kairo:kairo-serialization:0.3.0")
}
```

### Step 2: Create and use the object mapper

```kotlin
// src/main/kotlin/yourPackage/YourFile.kt

val mapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()
// Change ObjectMapperFormat.Json to use other formats, such as YAML.

// Serialize.
val myClass: MyClass = TODO()
mapper.writeValueAsString(myClass)

// Deserialize.
val myString: String = TODO()
mapper.readValue<MyClass>(myString) // Be sure to import readValue.
```

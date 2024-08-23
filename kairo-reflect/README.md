# `kairo-reflect`

This is a wrapper for Kotlin's reflection library
that also includes some reflection-related utilities.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  testImplementation("kairo:kairo-reflect:0.4.0")
}
```

### Step 2: Try out the `typeParam` util

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

abstract class ExampleClass<T : Any> {
  val typeParam: KClass<T> = typeParam(0, ExampleClass::class, this::class)
}

class ExampleIntSubclass : ExampleClass<Int>()

ExampleIntSubclass().typeParam // This will be Int::class.
```

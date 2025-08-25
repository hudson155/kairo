# Kairo Reflect

Proxies Kotlin's reflection library,
with the addition of the runtime-rich `KairoType<T>` class.

## Installation

Get started by installing `kairo-reflect`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-reflect:6.0.0")
}
```

## Usage

### `kairoType`

Includes context from `Class<T>`, `KClass<T>`, Java's `Type`, and Kotlin's `KType`.

```kotlin
val type = kairoType<MyClass<String>>()
```

### `KairoType.from()`

Allows you to access the concrete `KClass<T>` or `KType` of a generic type parameter at runtime.
Preserves full generic info (`List<String>`, not just `List<*>`).

```kotlin
abstract class MyClass<T : Any> {
  val type: KairoType<T> = inferType(MyClass::class, 0, this::class)
}
```

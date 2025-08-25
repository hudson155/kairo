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
Preserves full generic info (`List<String>`, not just `List<*>`).

```kotlin
val type = kairoType<MyClass<String>>()
```

### `KairoType.from()`

Infers `KairoType<T>` of a generic class type parameter at runtime.
Preserves full generic info (`List<String>`, not just `List<*>`).

```kotlin
abstract class MyClass<T : Any> {
  val type: KairoType<T> = KairoType.from(MyClass::class, 0, this::class)
}
```

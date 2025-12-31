# Kairo Utilities

A home for simple utilities.

Utilities modules like this can easily become junk drawers;
we make an effort to keep `kairo-util` small and focused.

## Installation

Install `kairo-util`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-util")
}
```

## Usage

The most popular functions are documented here.

### `canonicalize()` and `slugify()`

`canonicalize()` turns an arbitrary string into exclusively lowercase latin characters.

```kotlin
canonicalize(" Con  | dãnas^t")
// => con danast
```

`slugify()` works similarly, but allows you to choose the delimiter.
```kotlin
slugify(" Con  | dãnas^t", delimiter = "-")
// => con-danast
```

### `firstCauseOf<T>()`

For exceptions,
returns the first recursive cause of the specified type, or null if there is no matching cause.

```kotlin
e.firstCauseOf<MyExceptionType>()
```

### `resource()`

Leverages Guava to fetch a given resource URL.

```kotlin
val resource = resource("...")
println(resource)
```

_Note: This uses Guava under the hood.
Guava is a large library and is **not** included in this artifact.
Usage of `resource()` requires Guava to be available at runtime._

### `singleNullOrThrow()`

Kotlin's `single()` and `singleOrNull()` functions are excellent utilities,
but `singleOrNull()` can be surprising because it returns `null` when there are multiple items.

`singleNullOrThrow()` returns the single element, or null if the collection is empty.
If the collection has more than one element, throws `IllegalArgumentException`.

This works with `Array`, `Iterable`, and `Sequence` receivers,
as well as `Flow` through [kairo-coroutines](../kairo-coroutines/README.md).

```kotlin
emptyList<Int>().singleNullOrThrow()
// => null

listOf(1).singleNullOrThrow()
// => 1

listOf(1, 2).singleNullOrThrow()
// => IllegalArgumentException
```

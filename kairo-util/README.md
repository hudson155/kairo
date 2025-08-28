# Kairo Utilities

This is a home for various simple utilities.
Utilities modules like this can easily become junk drawers;
we make an effort to keep this module small and focused.

## Installation

Get started by installing `kairo-util`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-util")
}
```

## Usage

### `singleNullOrThrow()`

Kotlin's `single()` and `singleOrNull()` functions are excellent utilities,
but `singleOrNull()` can be surprising because it returns `null` when there are multiple items.

`singleNullOrThrow()` returns the single element, or null if the collection is empty.
If the collection has more than one element, throws `IllegalArgumentException`.

This works with `Array`, `Iterable`, and `Sequence` receivers.
Feel free to [open an issue](https://github.com/hudson155/kairo/issues/new)
if you need other receiver types.

```kotlin
emptyList<Int>().singleNullOrThrow()
// => null

listOf(1).singleNullOrThrow()
// => 1

listOf(1, 2).singleNullOrThrow()
// => IllegalArgumentException
```

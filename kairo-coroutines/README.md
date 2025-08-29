# Kairo Coroutines

`kairo-coroutines` extends Kotlin's coroutine library.

- Provides **[Arrow's coroutines library](https://arrow-kt.io/learn/coroutines/)**,
  which includes coroutine-safe concurrency primitives like `CountDownLatch` and `CyclicBarrier`.
- Adds some **convenient helper functions**.

## Installation

Get started by installing `kairo-coroutines`.\
You don't need to install Kotlin or Arrow's coroutines library separately —
they're included by default.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-coroutines")
}
```

## Usage

### Arrow's concurrency primitives

Arrow's concurrency primitives are coroutine-safe, unlike the JVM-default ones.

#### `CountDownLatch`

`CountDownLatch` allows for awaiting a given number of countdown signals.

```kotlin
val latch = CountDownLatch(10)
List(10) { latch.await() }
```

#### `CyclicBarrier`

A `CyclicBarrier` is a synchronization mechanism that allows a set of coroutines to wait for each other
to reach a certain point before continuing execution.

```kotlin
val barrier = CyclicBarrier(10)
List(10) { barrier.await() }
```

### Helper functions

### `singleNullOrThrow()`

Kotlin's `single()` and `singleOrNull()` functions are excellent utilities,
but `singleOrNull()` can be surprising because it returns `null` when there are multiple items.

`singleNullOrThrow()` returns the single element, or null if the collection is empty.
If the collection has more than one element, throws `IllegalArgumentException`.

[kairo-util](../kairo-util) already exposes this function with `Array`, `Iterable`, and `Sequence` receivers.
This library exposes it with the `Flow` receiver.
Feel free to [open an issue](https://github.com/hudson155/kairo/issues/new)
if you need other receiver types.

```kotlin
emptyFlow<Int>().singleNullOrThrow()
// => null

flowOf(1).singleNullOrThrow()
// => 1

flowOf(1, 2).singleNullOrThrow()
// => IllegalArgumentException
```

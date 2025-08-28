# DARB

DARB stands for "dense-ish albeit readable binary".

It's a compact way to encode boolean lists into short strings
while still keeping them human-readable.
The primary use case is **shrinking long permission lists inside JWTs**,
but DARB can be used anywhere you need a balance of efficiency and human-readability.

### Why DARB?

- **JWT-friendly:** Dramatically reduces the size of tokens with long boolean/permission lists.
- **Readable at a glance:** Unlike raw binary or base 64,
  you can still tell the list length and understand its contents without tooling.
- **Space savings:** Up to 75% smaller than a naive string representation.

_Note: Don't use this library for HTTP response body compression.
There are already great HTTP compression libraries for that._

### By example...

```kotlin
val darb = "23.2CB08E"

val booleanList = listOf(
  false, false, true, false,
  true, true, false, false,
  true, false, true, true,
  false, false, false, false,
  true, false, false, false,
  true, true, true,
)
```

- The **prefix** (`23` in the example above) is the length of the list.
- The **body** (`2CB08E` in the example above),
  where each character represents up to 4 booleans.
  - `2` becomes`[false, false, true, false]`
  - `C` becomes`[true, true, false, false]`
  - `B` becomes `[true, false, true, true]`
  - `0` becomes `[false, false, false, false]`
  - `8` becomes `[true, false, false, false]`
  - `E` becomes `[true, true, true, false]`
    (last boolean ignored, since prefix is `23`).

For this example, DARB uses **62.5% less space** than a plain binary string.
For larger lists, the savings approach **75%**.

## Installation

Get started by installing `kairo-darb`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-darb")
}
```

## Usage

To encode a boolean list, use `DarbEncoder.encode()`.

```kotlin
DarbEncoder.encode(
  listOf(
    false, false, true, false,
    true, true, false, false,
    true, false, true, true,
    false, false, false, false,
    true, false, false, false,
    true, true, true,
  ),
)
// => 23.2CB08E
```

To decode a DARB string, use `DarbDecoder.decode()`.

```kotlin
DarbDecoder.decode("23.2CB08E")
// => listOf(false, false, true, false, ...)
```

## Logging config

We recommend excluding logs below the `INFO` level for this library.

```xml
<Logger name="kairo.darb" level="info"/>
```

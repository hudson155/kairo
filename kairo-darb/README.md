# DARB

## Overview

DARB stands for "dense-ish albeit readable binary".
It's a medium-density way to encode boolean lists
while maintaining human-readability.
The main intention here is to cut down on JWT token size
(especially when including long permission lists in the JWT),
but it could be used for other purposes as well.

_Note: Don't use this library for HTTP response body compression.
There are already great HTTP compression libraries for that._

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

A DARB string contains 2 components, a prefix and a body, separated by a dot.
In the example above, the DARB string `23.2CB08E` has a prefix of `23` and a body of `2CB08E`.

- The **prefix** indicates the length of the boolean list.
  In this case, there are 23 booleans.
  It's therefore very easy for humans to understand the length of a decoded DARB string by glancing at the encoded version.
- Within the **body**, each character represents up to 4 booleans.
  - The first character (`2`) maps to `[false, false, true, false]`.
  - The second character (`C`) maps to `[true, true, false, false]`.
  - The third character (`B`) maps to `[true, false, true, true]`.
  - The fourth character (`0`) maps to `[false, false, false, false]`.
  - The fifth character (`8`) maps to `[true, false, false, false]`.
  - The sixth character (`E`) maps to `[true, true, true, false]`.
    But because the length of this string is only `23`, we ignore the last boolean.

Compared to representation in a binary string, DARB uses 62.5% less space in this example.
For longer strings, this approaches 75%.

## Installation

Get started by installing `kairo-darb`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-darb:6.0.0")
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

# `kairo-darb`

Home of `DarbEncoder`, which encodes a list of booleans into a Dense-ish Albeit Readable Binary (DARB) string.
A DARB string contains 2 components, a prefix and a body, separated by a dot.

An example DARB string is `5.C8`.
This string represents the boolean list `true, true, false, false, true`.
The prefix is `5` and the body is `C8`.

The prefix indicates the length of the data.
Within the body, each character represents 4 booleans
(except the last character, which can represent fewer than 4 but not 0 booleans).
In the example above, the first character "C" maps to 1100 in binary, which represents `true, true, false, false`.
The second character "8" maps to 1000 in binary, which represents `true, false, false, false`.
However, since there are only 5 booleans in the list (indicated by the prefix), we ignore the trailing booleans.

**Examples**

| DARB   | Boolean list                        |
|--------|-------------------------------------|
| `0.`   |                                     |
| `1.0`  | `false`                             |
| `1.8`  | `true`                              |
| `4.0`  | `false, false, false, false`        |
| `4.1`  | `false, false, false, true`         |
| `4.8`  | `true, false, false, false`         |
| `4.F`  | `true, true, true, true`            |
| `5.00` | `false, false, false, false, false` |
| `5.08` | `false, false, false, false, true`  |
| `5.80` | `true, false, false, false, false`  |
| `5.C8` | `true, true, false, false, true`    |
| `5.F8` | `true, true, true, true, true`      |

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  api("kairo:kairo-darb:0.3.0")
}
```

### Step 2: Encode and decode

```kotlin
// src/main/kotlin/yourPackage/feature/task/TaskFeature.kt

DarbEncoder.encode(listOf(true, true, false, false, true)) // "5.C8"
DarbEncoder.decode("5.C8") // listOf(true, true, false, false, true)
```

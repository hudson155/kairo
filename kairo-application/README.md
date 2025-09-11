# Kairo Application

**Start** your Server,
**wait** for JVM termination,
and **clean up** afterwards,
all with a single call:
`startAndWait()`.

### Sample usage

Sample usage from the [kairo-sample](https://github.com/hudson155/kairo-sample) repo.

- [Main.kt](https://github.com/hudson155/kairo-sample/blob/main/src/main/kotlin/kairoSample/Main.kt)

## Installation

Install `kairo-application`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-application")
}
```

## Usage

Basic usage: call `startAndWait()` inside a `kairo` block.

```kotlin
fun main() {
  kairo {
    val server = Server(...)

    server.startAndWait(
      release = {
        server.stop()
      },
    )
  }
}
```

The `release` function is called when the JVM terminates.

### Closing other resources.

Kairo Features shut themselves down gracefully when you call `server.stop()`.
If you have other resources that need to be closed,
you can also use the release block for that.

```kotlin
fun main() {
  kairo {
    val server = Server(...)
    server.startAndWait(
      release = {
        server.stop()
        LogManager.shutdown() // Shuts down Log4j2 gracefully.
      },
    )
  }
}
```

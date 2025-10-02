# Kairo Application

**Start** your Server,
**wait** for JVM termination,
and **clean up** afterwards,
all with a single call:
`startAndWait()`.

## Installation

Install `kairo-application`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-application")
}
```

## Usage

Call `startAndWait()` inside a `kairo` block.

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

#### A note about Log4j2 and Server shutdown

Log4j2 automatically installs a shutdown hook that listens for JVM termination,
flushing buffers and ignoring future logging calls.
This means that you aren't guaranteed to see any log messages after the JVM terminates.
To see Kairo shutdown messages in your logs,

1. Add `shutdownHook="disable"` to your `log4j2.xml` file.
2. Add `LogManager.shutdown()` to your `release` block.

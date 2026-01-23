# Kairo Logging

`kairo-logging` standardizes on SLF4J,
exposing [Ohad Shai's Kotlin logging interface](https://github.com/oshai/kotlin-logging)
for a clean, Kotlin-first API.

**SLF4J means zero lock-in** — change your logging backend (Log4j2, Logback, etc.)
without changing your code.

## Installation

Install `kairo-logging`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-logging")
}
```

You'll also need to add an SLF4J backend to your runtime classpath.
We recommend Log4j2, which you can configure using XML.
But you can use anything (really)!

## Usage

Define and use your logger using the idiomatic API.

```kotlin
private val logger: KLogger = KotlinLogging.logger {}

fun main() {
  logger.info { "Starting application..." }
  logger.warn { "Disk space is running low (currentDiskSpace=$currentDiskSpace)." }
  logger.error(e) { "Could not send notification." }
}
```

### Example: `slf4j-simple`

Using `slf4j-simple` as the backend lets you turn on logs with zero configuration.
This is great for simple projects.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-logging")
  runtimeOnly("org.slf4j:slf4j-simple")
}
```

### Example: Log4j2

For production applications, we recommend Log4j2.
Or you could use Logback.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-logging")
  runtimeOnly("org.apache.logging.log4j:log4j-core")
  runtimeOnly("org.apache.logging.log4j:log4j-slf4j2-impl")
}
```

```xml
<!-- log4j2.xml -->

<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="warn">
  <Appenders>
    <Console name="Console" target="SYSTEM_OUT">
      <PatternLayout>
        <Pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p %c - %m %mdc%n</Pattern>
      </PatternLayout>
    </Console>
  </Appenders>
  <Loggers>
    <Root level="${env:LOG_LEVEL:-info}">
      <AppenderRef ref="Console"/>
    </Root>
    <Logger name="io.ktor.server.plugins.cors.CORS" level="warn"/>
    <Logger name="io.r2dbc.postgresql.client.ReactorNettyClient" level="info"/>
    <Logger name="kairo.darb" level="info"/>
    <Logger name="kairo.gcpSecretSupplier" level="info"/>
    <Logger name="kairo.rest" level="info"/>
  </Loggers>
</Configuration>
```

#### A note about Log4j2 and Server shutdown

Log4j2 automatically installs a shutdown hook that listens for JVM termination,
flushing buffers and ignoring future logging calls.
This means that you aren't guaranteed to see any log messages after the JVM terminates.
To see Kairo shutdown messages in your logs,

1. Add `shutdownHook="disable"` to your `log4j2.xml` file.
2. Add `LogManager.shutdown()` to your `release` block.

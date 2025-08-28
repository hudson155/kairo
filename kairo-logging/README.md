# Kairo Logging

Logging should be **lightweight, idiomatic, and configurable**.
`kairo-logging` standardizes on SLF4J,
exposing [Ohad Shai's Kotlin logging interface](https://github.com/oshai/kotlin-logging)
for a clean, Kotlin-first API.

**SLF4J means zero lock-in** — change your logging backend (Log4j2, Logback, etc.)
without changing your code.

## Installation

Get started by installing `kairo-logging`.

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
  testRuntimeOnly("org.slf4j:slf4j-simple")
  implementation("software.airborne.kairo:kairo-logging")
}
```

### Example: Log4j2

For production applications, we recommend Log4j2.
You could also use Logback.

```kotlin
// build.gradle.kts

dependencies {
  testRuntimeOnly("org.apache.logging.log4j:log4j-core")
  testRuntimeOnly("org.apache.logging.log4j:log4j-slf4j2-impl")
  implementation("software.airborne.kairo:kairo-logging")
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
    <Logger name="kairo.darb" level="info"/>
    <Logger name="kairo.gcpSecretSupplier" level="info"/>
  </Loggers>
</Configuration>
```

# Kairo Logging

Logging should be **consistent, lightweight, and idiomatic**.
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

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="warn">
  <Appenders>
    <Routing name="Console">
      <Routes pattern="$${env:LOG_FORMAT:-Simple}">
        <Route key="Simple">
          <Console name="Simple" target="SYSTEM_OUT">
            <PatternLayout>
              <Pattern>%d{HH:mm:ss} %-5p %m%n</Pattern>
            </PatternLayout>
          </Console>
        </Route>
        <Route key="Detailed">
          <Console name="Detailed" target="SYSTEM_OUT">
            <PatternLayout>
              <Pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p %c - %m %mdc%n</Pattern>
            </PatternLayout>
          </Console>
        </Route>
        <Route key="GcpJson">
          <Console name="GcpJson" target="SYSTEM_OUT">
            <JsonTemplateLayout eventTemplateUri="classpath:GcpLayout.json"/>
          </Console>
        </Route>
      </Routes>
    </Routing>
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

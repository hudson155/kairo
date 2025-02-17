# `kairo-logging`

**Refer to the [kairo-sample](https://github.com/hudson155/kairo-sample) repository to see it in action.**

Logging uses the [kotlin-logging](https://github.com/oshai/kotlin-logging) interface,
which should be configured to use Apache Log4j2 under the hood.

Please also install [kairo-logging-feature](../kairo-logging-feature)
if you're using this within the context of a Kairo Server.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-logging:$kairoVersion")
}
```

### Step 2: Configure logging

YAML is a bit nicer than XML.
Notice that the config includes `%mdc` in the pattern, which Kairo uses extensively.
You may wish to change from a console appender to a different appender depending on your environment.

```yaml
# src/main/resources/log4j2.yaml

Configuration:
  status: info # For internal Log4j2 logs.
  Appenders:
    Console:
      name: Plaintext
      target: SYSTEM_OUT
      PatternLayout:
        # This is a pretty decent pattern, but change it if you wish.
        pattern: "%date{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %c - %message %mdc%n"
  Loggers:
    Root:
      level: "${env:LOG_LEVEL:-info}"
      AppenderRef:
        ref: Plaintext
```

### Step 3: Use logging

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

private val logger: KLogger = KotlinLogging.logger {} // Best to specify the type [KLogger] explicitly.

logger.debug { "Some debug message." }
logger.info { "The value of foo is $foo." }
logger.warn(e) { "Something went wrong but it's recoverable." }
logger.error(e) { "Something went very wrong." }
```

# `kairo-logging`

Logging uses the [kotlin-logging](https://github.com/oshai/kotlin-logging) interface,
which should be configured to use Apache Log4j2 under the hood.

Please also install [kairo-logging-feature](/kairo-logging-feature/)
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
Notice that both of the configs include `%mdc` in the pattern, which Kairo uses extensively.
You may wish to change from a console appender to a different appender depending on your environment.

```yaml
# src/main/resources/log4j2.yaml

Configuration:
  status: info # For internal Log4j2 logs.
  Appenders:
    Console:
      type: Console
      name: Plaintext
      target: SYSTEM_OUT
      PatternLayout: # This is a pretty decent pattern, but change it if you wish.
        pattern: "%date{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %c - %message %mdc%n"
  Loggers:
    Root:
      level: info # You may wish to change this line.
      AppenderRef:
        ref: Plaintext
```

The test config is almost identical except for `Configuration.Loggers.Root.level`.

```yaml
# src/test/resources/log4j2-test.yaml

Configuration:
  status: info # For internal Log4j2 logs.
  Appenders:
    Console:
      type: Console
      name: Plaintext
      target: SYSTEM_OUT
      PatternLayout:
        # This is a pretty decent pattern, but change it if you wish.
        pattern: "%date{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %c - %message %mdc%n"
  Loggers:
    Root:
      level: debug # You may wish to change this line.
      AppenderRef:
        ref: Plaintext
```

### Step 3: Use logging

```kotlin
private val logger: KLogger = KotlinLogging.logger {} // Best to specify the type [KLogger] explicitly.

logger.debug { "Some debug message." }
logger.info { "The value of foo is $foo." }
logger.warn(e) { "Something went wrong but it's recoverable." }
logger.error(e) { "Something went very wrong." }
```

# Kairo

Kairo is an application framework built for Kotlin.

## Project information

### Major dependencies

- Gradle 8.9
- Kotlin 2.0.0
- Java 21
- Ktor 3.0.0-beta-2
- Guice 7.0.0
- Jackson 2.17.2

### Brand guidelines

- Treat Kairo _Features_ and _Servers_ as proper nouns (the first letter should be capitalized).

## Modules

- [kairo-config](kairo-config/):
  Home of `ConfigLoader`, which loads configs from YAML files,
  with support for config extension and application.
- [kairo-darb](kairo-darb/):
  Home of `DarbEncoder`, which encodes a list of booleans into a Dense-ish Albeit Readable Binary (DARB) string.
- [kairo-dependency-injection](kairo-dependency-injection/):
  Makes Guice available,
  along with some utilities to make its use more idiomatic.
- [kairo-feature](kairo-feature/):
  Features are the primary building block of Kairo applications.
- [kairo-logging](kairo-logging/):
  Logging uses the [kotlin-logging](https://github.com/oshai/kotlin-logging) interface,
  which should be configured to use Apache Log4j2 under the hood.
- [kairo-protected-string](kairo-protected-string/):
  `ProtectedString` represents a string value that should not be logged or otherwise exposed.
- [kairo-rest-feature](kairo-rest-feature/):
  The REST Feature adds support for REST endpoints.
  Under the hood, this Feature uses [Ktor](https://ktor.io/).
- [kairo-serialization](kairo-serialization/):
  Uses [Jackson](https://github.com/FasterXML/jackson)
  to handle JSON and YAML serialization
- [kairo-server](kairo-server/):
  A Server is an application that runs a set of Features.
- [kairo-testing](kairo-testing/):
  A convenient testing library which includes some helpful test dependencies.

## Getting started

### Step 1: Add required dependencies

```kotlin
// build.gradle.kts

dependencies {
  // Basic dependencies.
  api("kairo:kairo-server:0.4.0")
  implementation("kairo:kairo-logging:0.4.0")

  // Enable REST endpoints.
  api("kairo:kairo-rest-feature:0.4.0")

  // Test-related dependencies.
  testImplementation("kairo:kairo-testing:0.4.0")
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
      PatternLayout:
        # This is a pretty decent pattern, but change it if you wish.
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

### Step 3: Create your first Feature

This is an example `LibraryFeature` for managing a public library.
`yourPackage` represents whatever top-level package you're using for your project.

```kotlin
// src/main/kotlin/yourPackage/feature/library/LibraryFeature.kt

class LibraryFeature : Feature() {
  override val name: String = "Library"

  // Use "Normal" for most Features.
  override val priority: FeaturePriority = FeaturePriority.Normal
  
  override fun bind(binder: PrivateBinder) {
    // Do your Guice binding here!
  }
}
```

### Step 4: Create your first Server

You can either run all of your Features on a single Server (monolithic architecture),
or spread them out across several Servers (microservices architecture).
This example is a monolith.

First, create the config class and YAML.
Regardless of whether you're building a monolith or microservices,
you'll need to configure some basic properties like `featureManager` and `restFeature`.

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServerConfig.kt

data class MonolithServerConfig(
  val featureManager: FeatureManagerConfig,
  val restFeature: RestConfig,
)
```

```yaml
featureManager:
  lifecycle:
    startupDelayMs: 2000 # 2 seconds.
    shutdownDelayMs: 4000 # 4 seconds.

restFeature:
  connector:
    host: "0.0.0.0"
    port: 8080
  lifecycle:
    shutdownGracePeriodMs: 15_000 # 15 seconds.
    shutdownTimeoutMs: 25_000 # 25 seconds.
  parallelism:
    connectionGroupSize: 16
    workerGroupSize: 32
    callGroupSize: 64
```

Finally, create the Server class itself.

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

class MonolithServer(
  config: MonolithServerConfig,
) : Server() {
  override val featureManager: FeatureManager =
    FeatureManager(
      features = setOf(
        LibraryFeature(),
        RestFeature(config.restFeature),
      ),
      config = config.featureManager,
    )
}

fun main() {
  val config = ConfigLoader.load<MonolithServerConfig>()
  val server = MonolithServer(config)
  server.start()
}
```

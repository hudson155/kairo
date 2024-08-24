# `kairo-server`

A Server is an application that runs a set of Features.

Note: Treat Kairo _Servers_ as proper nouns (the first letter should be capitalized).

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  api("kairo:kairo-server:0.5.0")
}
```

### Step 2: Create your first Server

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

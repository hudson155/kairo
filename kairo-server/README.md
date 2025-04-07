# `kairo-server`

**Refer to the [kairo-sample](https://github.com/hudson155/kairo-sample) repository to see it in action.**

A Server is an application that runs a set of Features.

Note: Treat Kairo _Servers_ as proper nouns (the first letter should be capitalized).

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  api("kairo:kairo-server:$kairoVersion")
}
```

### Step 2: Create your first Server

You can either run all of your Features on a single Server (monolithic architecture),
or spread them out across several Servers (microservices architecture).
This example is a monolith.

First, create the config class and YAML.
Regardless of whether you're building a monolith or microservices,
you'll need to configure some basic properties like `featureManager` and `rest`.

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServerConfig.kt

data class MonolithServerConfig(
  val featureManager: FeatureManagerConfig,
  val rest: KairoRestConfig,
)
```

```yaml
# src/main/resources/config/config.yaml

auth:
  jwtIssuer: "https://example.com/"
  jwtSecret: { source: "GcpSecret", id: "projects/012345678900/secrets/example/versions/1" }
  leewaySec: 20 # 20 seconds.

featureManager:
  lifecycle: {}

rest:
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
        KairoRestFeature(config.rest) {
          install(Auth) {
            kairoConfigure {
              add(
                JwtAuthVerifier(
                  schemes = listOf("Bearer"),
                  mechanisms = listOf(
                    JwtJwtAuthMechanism(
                      issuers = listOf(authConfig.jwtIssuer),
                      algorithm = Algorithm.HMAC256(authConfig.jwtSecret.value),
                      leewaySec = authConfig.leewaySec,
                    ),
                  ),
                ),
              )
            }
          }
        },
        LibraryFeature(),
      ),
      config = config.featureManager,
    )
}

fun main() {
  val config = ConfigLoader.createDefault().load<MonolithServerConfig>()
  val server = MonolithServer(config)
  server.start()
}
```

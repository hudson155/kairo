# Kairo Configs

Application configuration should be **simple, consistent, and powerful**.
`kairo-config` standardizes on [lightbend/config](https://github.com/lightbend/config)'s HOCON implementation,
giving every Kairo project a familiar, robust foundation for managing configs.

```hocon
# production.conf
include "common.conf" # Include another config file.
sql.connectionFactory {
  url = ${?POSTGRES_URL} # Environment variable.
  username = "kairo_sample"
  password = "gcp::projects/012345678900/secrets/example/versions/1" # Config resolver.
  ssl = false
}
```

### HOCON

- **Rich features**
  - Built-in config inheritance and overrides for easy multi-env (dev/staging/prod).
  - Native environment variable substitution.

- **Developer ergonomics:** Comments, human-readable syntax, less boilerplate.

### Config resolvers

Kairo's config resolvers let you pull in properties from other sources
like **Google Secret Manager**.

## Installation

Install `kairo-config`.
You don't need to install lightbend/config separately —
it's included by default.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-config")
}
```

## Reference implementation

The Kotlin data class is defined in [kairoSample/Config.kt](https://github.com/hudson155/kairo-sample/blob/main/src/main/kotlin/kairoSample/Config.kt),
and various HOCON files are defined.

- [config/development.conf](https://github.com/hudson155/kairo-sample/blob/main/src/main/resources/config/development.conf)
- [config/local-production.conf](https://github.com/hudson155/kairo-sample/blob/main/src/main/resources/config/local-production.conf)
- [config/production.conf](https://github.com/hudson155/kairo-sample/blob/main/src/main/resources/config/production.conf)

## Usage

[lightbend/config's documentation](https://github.com/lightbend/config)
explains HOCON and config management in detail.
Here's how to use it through Kairo.

### Your Kotlin config class

First, define your config class using `@Serializable`.

```kotlin
@Serializable
data class Config(
  val rest: RestFeatureConfig,
  val sql: SqlFeatureConfig,
)
```

#### Common (base) config

You'll probably have a base config with settings shared across environments.

```hocon
# common.conf
rest {
  connector.port = 8080
  plugins.defaultHeaders.serverName = "Kairo Sample"
}
```

#### Production config

Your production config will extend the base config,
specifying anything that was missing and/or overriding things that are different.

```hocon
# production.conf
include "common.conf"
sql.connectionFactory {
  url = ${?POSTGRES_URL} # Environment variable.
  username = "kairo_sample"
  password = ${?POSTGRES_PASSWORD} # Environment variable.
  ssl = false
}
```

You can create a staging config the same way.

#### Development config

Your development config will also extend the base config,
but override a few settings.

```hocon
# development.conf
include "common.conf"
rest {
  parallelism { runningLimit = 10, callGroupSize = 2 } # Reduced parallelism.
  plugins.callLogging.useColors = true # Nice local experience.
}
sql {
  connectionFactory {
    url = "r2dbc:postgresql://localhost/kairo_sample"
    username = ${?KAIRO_SAMPLE_POSTGRES_USERNAME}
    password = ${?KAIRO_SAMPLE_POSTGRES_PASSWORD}
    ssl = false
  }
  connectionPool.size { initial = 2, min = 1, max = 5 } # Reduced pool size.
}
```

### Loading the config

Back to your Kotlin code — load the config and deserialize it into your config class.

```kotlin
val config: Config =
  ConfigFactory.load("production.conf")
    .let { Hocon.decodeFromConfig(it) }
```

If you want to dynamically choose which config to load, use an environment variable.

```kotlin
val configName = requireNotNull(System.getenv("CONFIG"))
val config: Config =
  ConfigFactory.load("config/$configName.conf")
    .let { Hocon.decodeFromConfig(it) }
```

#### Config resolvers

To use config resolvers, call `resolveConfig()` after loading your config.

```kotlin
private val gcpSecretSupplier: GcpSecretSupplier = DefaultGcpSecretSupplier()

val configName = requireNotNull(System.getenv("CONFIG"))
val configResolver = ConfigResolver(
  resolvers = listOf(
    ConfigResolver.Resolver("gcp::") { gcpSecretSupplier[it]?.value },
  ),
)

val config: Config =
  ConfigFactory.load("config/$configName.conf")
    .let { Hocon.decodeFromConfig<Config>(it) }
    .let { ConfigResolver.resolve(it) }
```

This will detect any strings (or `ProtectedString`s) that start with `gcp::`,
resolving them using the `GcpSecretSupplier`.

Now you could change your production config to pull GCP secrets.

```hocon
# production.conf
include "common.conf"
sql.connectionFactory {
  url = ${?POSTGRES_URL}
  username = "kairo_sample"
  password = "gcp::projects/012345678900/secrets/example/versions/1" # Config resolver.
  ssl = false
}
```

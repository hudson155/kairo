# Kairo Configs

Application configuration should be **simple, consistent, and powerful**.\
`kairo-config` standardizes on [lightbend/config](https://github.com/lightbend/config)'s HOCON implementation,
giving every Kairo project a familiar, robust foundation for managing configs.

```hocon
include "common.conf" # Include another config file.
app {
  port = 8080
}
sentryDsn = ${?SENTRY_DSN} # Optional env var.
databasePassword = ${DATABASE_PASSWORD} # Required env var.
accessToken = "gcp::projects/012345678900/secrets/example/versions/1" # Config resolver.
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

Install `kairo-config`.\
You don't need to install lightbend/config separately —
it's included by default.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-config")
}
```

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

#### Base/common config

You'll probably have a base/common config with settings shared across environments.

```hocon
# common.conf
rest {
  connector.port = 8080
  plugins.defaultHeaders.serverName = "..."
}
sql.connectionFactory {
  url = ${POSTGRES_URL} # Env var
  username = ${POSTGRES_USERNAME} # Env var
  password = ${POSTGRES_PASSWORD} # Env var
}
```

#### Production config

Your production config will extend the base config.
In this case, there's nothing to override.
Just make sure the Postgres-related environment variables are set at runtime.

```hocon
# production.conf
include "common.conf"
```

You can create a staging config the same way.

#### Development config

Your development config will also extend the base config,
but override a few settings.

```hocon
# development.conf
include "common.conf"
rest {
  parallelism { runningLimit = 10, callGroupSize = 2 }
  plugins.callLogging.useColors = true
}
sql {
  connectionFactory.url = "jdbc:postgresql://localhost:5432/kairo_sample"
  connectionPool.size { initial = 2, min = 1, max = 5 }
}
```

#### Testing config

It's usually easiest to make your testing config extend the development config instead of the base config,
since the overrides we applied to the development config also make sense for testing.

```hocon
# testing.conf
include "development.conf"
id.generation.type = "Deterministic"
sql.connectionFactory.url = "jdbc:postgresql://localhost:5432/kairo_sample_test"
```

### Loading the config

Back to your Kotlin code — load the config and deserialize it into your config class.

```kotlin
val config: Config =
  ConfigFactory.load("config/production.conf")
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

val configName = requireNotNull(System.getenv("CONFIG")) { "CONFIG environment variable not set." }

val configResolvers = listOf(
  ConfigResolver("gcp::") { gcpSecretSupplier[it]?.value },
)

val config: Config =
  ConfigFactory.load("config/$configName.conf")
    .let { Hocon.decodeFromConfig<Config>(it) }
    .let { resolveConfig(it, configResolvers) }
```

This will detect any strings (or `ProtectedString`s) that start with `gcp::`,
resolving them using the `GcpSecretSupplier`.

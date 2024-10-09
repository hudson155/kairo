# Kairo

Kairo is an application framework built for Kotlin.

## Project information

### Major dependencies

- Gradle 8.10
- Kotlin 2.0
- Java 21
- Ktor 3.0
- Guice 7.0
- Jackson 2.18

### Style guide

- **Product terminology:**
  Treat Kairo _Features_ and _Servers_ as proper nouns (the first letter should be capitalized).

See the [style guide](./docs/style-guide.md) for more.

### Chores

See [chores](./docs/chores.md).

## Modules

Kairo is split up into several separate modules.
You will need a number of these.
Each module falls into one of 3 categories:

1. **Common modules:**
   These are intended for your use.
   You will use some or all of these.
   API stability is guaranteed.
2. **Optional modules:**
   You may use these modules, but they won't always be necessary.
   API stability is guaranteed.
3. **Internal modules:**
   These are only intended for internal use.
   Use them at your own risk; API stability is not guaranteed.

### Common modules

**Core modules:**

- [kairo-server](./kairo-server):
  A Server is an application that runs a set of Features.
- [kairo-feature](./kairo-feature):
  Features are Kairo's main building block.
- [kairo-logging](./kairo-logging):
  Logging uses the [kotlin-logging](https://github.com/oshai/kotlin-logging) interface,
  which should be configured to use Apache Log4j2 under the hood.
- [kairo-config](./kairo-config):
  Home of `ConfigLoader`, which loads configs for Kairo Servers from YAML files.
  Includes support for config extension and application,
  as well as various sources.
- [kairo-serialization](./kairo-serialization):
  Uses [Jackson](https://github.com/FasterXML/jackson).
  to handle JSON and YAML serialization.
- [kairo-testing](./kairo-testing):
  A convenient testing library which includes some helpful test dependencies.
- [kairo-util](./kairo-util):
  Some useful utilities.

**Feature modules:**

- [kairo-clock-feature](./kairo-clock-feature):
  The Clock Feature allows for configuration and injection of a Java `Clock`.
- [kairo-google-app-engine-feature](./kairo-google-app-engine-feature):
  Kairo's Google App Engine Feature uses the health check readiness endpoint
  to configure a GAE warmup endpoint.
- [kairo-health-check-feature](./kairo-health-check-feature):
  Kairo's Health Check Feature configures liveness and readiness health check REST endpoints.
- [kairo-id-feature](./kairo-id-feature):
  Kairo IDs are an optional way to uniquely identify entities.
  Think of them as an alternative to `UUID`s or serial IDs, but with a few perks.
  Includes testable ID generation code.
- [kairo-logging-feature](./kairo-logging-feature):
  This Feature supports logging within Kairo Servers.
- [kairo-rest-feature](./kairo-rest-feature):
  The REST Feature adds support for REST endpoints, including auth.
  Under the hood, this Feature uses [Ktor](https://ktor.io/).
- [kairo-sql-feature](./kairo-sql-feature):
  The SQL Feature adds support for SQL databases.
  Under the hood, this Feature uses [JDBI](https://jdbi.org/).
- [kairo-sql-migration-feature](./kairo-sql-migration-feature):
  The SQL Migration Feature complements [kairo-sql-feature](./kairo-sql-feature).
  by adding support for database migrations.
  Under the hood, this Feature uses [Flyway Community](https://www.red-gate.com/products/flyway/community/).
- [kairo-uuid-feature](./kairo-uuid-feature):
  Includes testable UUID generation code.

### Optional modules

- [kairo-darb](./kairo-darb):
  Home of `DarbEncoder`, which encodes a list of booleans into a Dense-ish Albeit Readable Binary (DARB) string.
- [kairo-dependency-injection](./kairo-dependency-injection):
  Makes Guice available,
  along with some utilities to make its use more idiomatic.
- [kairo-exception](./kairo-exception):
  Base classes representing exceptions that Kairo knows how to handle.
  This is primarily used by [kairo-rest-feature](./kairo-rest-feature).
- [kairo-protected-string](./kairo-protected-string):
  `ProtectedString` represents a string value that should not be logged or otherwise exposed.
- [kairo-reflect](./kairo-reflect):
  This is a wrapper for Kotlin's reflection library
  that also includes some reflection-related utilities.
- [kairo-rest-client](./kairo-rest-client):
  A thin wrapper around [Ktor](https://ktor.io/)'s `HttpClient`.
- [kairo-transaction-manager](./kairo-transaction-manager):
  The Kairo `TransactionManager` offers explicit automatic handling of transactions that span multiple systems.
- [kairo-updater](./kairo-updater):
  A utility library for updating existing entities
  that supports `Optional`s to differentiate between `null` and `undefined` from the frontend.

### Internal modules

- [kairo-command-runner](./kairo-command-runner):
  `CommandRunner` runs shell commands.
  It delegates to Java's built-in way of doing this,
  but uses an abstract class for testability.
- [kairo-environment-variable-supplier](./kairo-environment-variable-supplier):
  `EnvironmentVariableSupplier` supplies environment variables.
  It delegates to Java's built-in way of doing this,
  but uses an abstract class for testability.
- [kairo-gcp-secret-supplier](./kairo-gcp-secret-supplier):
  `GcpSecretSupplier` supplies GCP secrets.
  It delegates to the GCP Secret Manager SDK,
  but uses an abstract class for testability.

## Getting started

### Step 1: Add dependencies

```kotlin
// build.gradle.kts

dependencies {
  api("kairo:kairo-clock-feature:$kairoVersion")
  api("kairo:kairo-config:$kairoVersion")
  api("kairo:kairo-health-check-feature:$kairoVersion")
  api("kairo:kairo-id-feature:$kairoVersion")
  api("kairo:kairo-logging-feature:$kairoVersion")
  api("kairo:kairo-rest-feature:$kairoVersion")
  api("kairo:kairo-rest-feature-content-negotiation:$kairoVersion")
  api("kairo:kairo-serialization:$kairoVersion")
  api("kairo:kairo-server:$kairoVersion")

  testImplementation("kairo:kairo-testing:$kairoVersion")
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
  shutdownHook: disable # Only include this line for Kairo Servers.
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
  shutdownHook: disable # Only include this line for Kairo Servers.
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
// src/main/kotlin/yourPackage/entity/libraryBook/LibraryBookRep.kt

data class LibraryBookRep(
  val id: KairoId,
  val title: String,
  val author: String?,
) {
  data class Creator(
    val title: String,
    val author: String?,
  )
}
```

```kotlin
// src/main/kotlin/yourPackage/entity/libraryBook/LibraryBookApi.kt

object TypicalLibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  data class Get(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep>()

  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  data class Create(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()
}
```

```kotlin
// src/main/kotlin/yourPackage/entity/libraryBook/LibraryBookHandler.kt

class LibraryBookHandler @Inject constructor() {
  inner class Get : RestHandler<LibraryBookApi.Get, LibraryBookRep>() {
    override suspend fun Auth.auth(endpoint: LibraryBookApi.Get): Auth.Result =
      TODO()

    override suspend fun handle(endpoint: LibraryBookApi.Get): LibraryBookRep {
      TODO()
    }
  }

  inner class Create : RestHandler<LibraryBookApi.Create, LibraryBookRep>() {
    override suspend fun Auth.auth(endpoint: LibraryBookApi.Create): Auth.Result =
      TODO()

    override suspend fun handle(endpoint: LibraryBookApi.Create): LibraryBookRep {
      TODO()
    }
  }
}
```

```kotlin
// src/main/kotlin/yourPackage/feature/library/LibraryFeature.kt

class LibraryFeature : Feature() {
  override val name: String = "Library"

  // Use "Normal" for most Features.
  override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: Binder) {
    binder.bindRestHandlers<LibraryBookHandler>()
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
  val clock: KairoClockConfig,
  val featureManager: FeatureManagerConfig,
  val id: KairoIdConfig,
  val logging: KairoLoggingConfig,
  val rest: KairoRestConfig,
)
```

```yaml
# src/main/resources/config/config.yaml
clock:
  type: "System"
  timeZone: "UTC"

featureManager:
  lifecycle:
    startupDelayMs: 2000 # 2 seconds.
    shutdownDelayMs: 4000 # 4 seconds.

id:
  generator:
    type: "Random"
    length: 22

logging:
  shutDownManually: true

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

sql:
  jdbcUrl: "jdbc:postgresql://localhost/monolith"
  properties: { }
  connectionTimeoutMs: 5000 # 5 seconds.
  minimumIdle: 16
  maximumPoolSize: 64 # Matches rest.parallelism.callGroupSize.
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
        KairoClockFeature(config.clock),
        KairoHealthCheckFeature(MonolithHealthCheckService::class),
        KairoIdFeature(config.id),
        KairoLoggingFeature(config.logging),
        KairoRestFeature(config.rest) {
          install(ContentNegotiation) {
            kairoConfigure()
          }
        },

        LibraryFeature(),
      ),
      config = config.featureManager,
    )
}

fun main() {
  val config = ConfigLoader.createDefault().load<MonolithServerConfig>("config")
  val server = MonolithServer(config)
  server.start()
}
```

## Releasing

1. Familiarize yourself with [semantic versioning](https://semver.org/).
2. Create a new branch called `release/X.Y.Z`.
3. Bump the version in [kairo-publish.gradle.kts](./buildSrc/src/main/kotlin/kairo-publish.gradle.kts).
4. Commit "Release X.Y.Z".
5. Create and merge a PR "Release X.Y.Z". No description is necessary.
6. [Draft a new release](https://github.com/hudson155/kairo/releases/new).
   Create a new tag `vX.Y.Z`.
   Title "Kairo X.Y.Z".
   Generate and prefix release notes.
7. Manually run `./gradlew publish` on `main` after merging.

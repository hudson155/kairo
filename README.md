# Kairo

Kairo is an application framework built for Kotlin.

## Project information

### Major dependencies

- Gradle 8.13
- Kotlin 2.1
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
  to handle JSON, YAML, and even XML serialization.
- [kairo-testing](./kairo-testing):
  A convenient testing library which includes some helpful test dependencies.
- [kairo-util](./kairo-util):
  Some useful utilities.

**Feature modules:**

- [kairo-clock-feature](./kairo-clock-feature):
  The Clock Feature allows for configuration and injection of a Java `Clock`.
- [kairo-google-cloud-scheduler-feature](./kairo-google-cloud-scheduler-feature):
  Kairo's Google Cloud Scheduler Feature enables CRON
  using Google Cloud Scheduler.
- [kairo-google-cloud-tasks-feature](./kairo-google-cloud-tasks-feature):
  Kairo's Google Cloud Tasks Feature enables task management
  using Google Cloud Tasks.
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
- [kairo-slack-feature](./kairo-slack-feature):
  Kairo's Slack Feature provides a basic interface to send Slack messages.
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

- [kairo-alternative-money-formatters](./kairo-alternative-money-formatters):
  Contains alternative non-default money formatters for serialization.
- [kairo-closeable](./kairo-closeable):
  `KairoCloseable` is similar to Java's `Closeable`, but it supports coroutines.
- [kairo-darb](./kairo-darb):
  Home of `DarbEncoder`, which encodes a list of booleans into a Dense-ish Albeit Readable Binary (DARB) string.
- [kairo-date-range](./kairo-date-range):
  Library for date range types.
- [kairo-dependency-injection](./kairo-dependency-injection):
  Makes Guice available,
  along with some utilities to make its use more idiomatic.
- [kairo-do-not-log-string](./kairo-do-not-log-string):
  `DoNotLogString` represents a string value that should not be logged in its entirety because it's very long.
  Similar to `ProtectedString` but with a different purpose.
- [kairo-exception](./kairo-exception):
  Base classes representing exceptions that Kairo knows how to handle.
  This is primarily used by [kairo-rest-feature](./kairo-rest-feature).
- [kairo-hashing](./kairo-hashing):
  Kairo's hashing library currently only supports simple MD5.
- [kairo-lazy-supplier](./kairo-lazy-supplier):
  Simple type-wrapping class that allows values to be provided and supplied lazily and asynchronously.
  Values will not be fetched more than once.
- [kairo-mdc](./kairo-mdc):
  Adds support for mapped diagnostic context (MDC),
  which is the JVM's way of adding context to log lines.
- [kairo-money](./kairo-money):
  The `Money` type is provided by JSR-354.
- [kairo-protected-string](./kairo-protected-string):
  `ProtectedString` represents a string value that should not be logged or otherwise exposed.
- [kairo-reflect](./kairo-reflect):
  This is a wrapper for Kotlin's reflection library
  that also includes some reflection-related utilities.
- [kairo-rest-client](./kairo-rest-client):
  A thin wrapper around [Ktor](https://ktor.io/)'s `HttpClient`.
- [kairo-time](./kairo-time):
  Contains some helpers an utilities for time classes.
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
- [kairo-google-common](./kairo-google-common):
  This common library contains adaptive utils
  for integrating Google's GCP libraries with Kotlin and Kairo.

## Getting started

The best way to get started is to refer to the
[kairo-sample](https://github.com/hudson155/kairo-sample) repository.

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

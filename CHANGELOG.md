# Kairo changelog

_Only non-bugfix release versions since Kairo 6.0 are included here._

## Kairo 6.0 - UNRELEASED

**Kairo 6.0** is a major release, with several breaking changes.

Kairo's goal has always been
to offer a set of libraries that work well together yet remain flexible and modular.
Previous versions of Kairo, however, fell short.
Libraries were too primitive or bespoke,
or were too tightly bound to specific design decisions.

Kairo 6 is a complete reset:
Every library has been completely reworked,
now truly living up to these ideals.

**Several libraries have been simplified
and many others have been eliminated entirely,
in favor of best-in-class external libraries that do the job far better.**

With Kairo 6,
there's also now a **bias toward Kotlin-first libraries instead of Java ones**,
reflecting the ecosystem's maturity since Kairo started in 2019.

### Kairo 6.0 highlights

- **Improved documentation.**
  Every library now has examples and testing guidance,
  reducing friction to adopt.

- **BOMs for dependency alignment.**
  Use `software.airborne.kairo:bom` for standalone libraries,
  or `software.airborne.kairo:bom-full` for Kairo applications.
  Keeps both Kairo and key external libraries in sync automatically.

- **Serialization with `kotlinx.serialization` (replaces Jackson).**
  Easier to use, fewer gotchas, and Kotlin-native.
  Fewer runtime surprises.

- **Dependency injection with [Koin](https://insert-koin.io/) (replaces Guice).**
  Reflection-free, Kotlin-friendly, better tooling, and simpler to configure.

- **Easier REST definition & routing.**
  Some custom Kairo syntax has been reverted to Ktor native routing syntax
  plus optional DSL helpers.

- **Type-safe SQL using [Exposed](https://www.jetbrains.com/exposed/)'s DSL (replaces JDBI).**
  - No more manual SQL strings,
    but retaining similar semantic alignment for predictability and easier debugging.

- **Switch to [HOCON](https://github.com/lightbend/config) for industry-standard configs.**
  - Great developer ergonomics (comments, human-readable syntax, less boilerplate).
  - Built-in config inheritance and overrides for easy multi-env (dev/staging/prod).
  - Native environment variable substitution.

- **Safer IDs with zero runtime cost.**
  Kairo IDs like `user_ccU4Rn4DKVjCMqt3d0oAw3` now have compile-time safety,
  meaning you can't mix up a user ID and a business ID.
  This is done without runtime overhead.

- **Simpler and faster integration testing.**
  - No need to spin up Ktor anymore — test the service layer directly.
  - Tests also run in parallel now!

### Full Kairo 6.0 changelog

#### Overall

- Upgrade Java from 21 to 25.
- Upgrade Gradle from 8 to 9.

#### Application

- Introduction of `kairo-application`
  lets you start your Server,
  wait for JVM termination,
  and clean up afterwards,
  all with a single call.

#### Client

No changes.

#### Config

- **Switch to [HOCON](https://github.com/lightbend/config) for industry-standard configs.**
  - Great developer ergonomics (comments, human-readable syntax, less boilerplate).
  - Built-in config inheritance and overrides for easy multi-env (dev/staging/prod).
  - Native environment variable substitution.

- Introduction of config resolvers,
  which let you pull in properties from other sources like **Google Secret Manager**.

#### Coroutines

- `singleNullOrThrow()` now works with Kotlin `Flow`.
- Introduction of `emitAll()` for `Iterable`.

#### DARB

No changes.

#### Datetime

- Introduced [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime).

#### Dependency Injection

- **Dependency injection with [Koin](https://insert-koin.io/) (replaces Guice).**
  Reflection-free, Kotlin-friendly, better tooling, and simpler to configure.

#### Exception

- Introduction of **logical failures**
  to describe situations not deemed successful in your domain,
  but still within the realms of that domain.
  - JSON serialization of logical failures.
  - Easily testable.

#### Feature

- **Features now start and stop in parallel,**
  improving Server performance.

- Feature lifecycle handlers have been refactored.

#### GCP Secret Supplier

- **GCP secrets are now fetched asynchronously on coroutines.**
  No blocking threads, and you can fetch multiple in parallel

- `FakeGcpSecretSupplier` for easier testing.

#### Google App Engine

- **Google App Engine is no longer supported.**
  For apps previously running on Google App Engine,
  consider using Google Cloud Run instead.

#### Health Check

- **Health checks now reflect actual readiness.**
  They won't pass until Ktor can serve traffic.

- Health checks now run in parallel.

- Health checks now have timeouts.

#### ID

- **Safer IDs with zero runtime cost.**
  Kairo IDs like `user_ccU4Rn4DKVjCMqt3d0oAw3` now have compile-time safety,
  meaning you can't mix up a user ID and a business ID.
  This is done without runtime overhead.

- The valid entropy range has been expanded.

- Testing now uses random IDs instead of deterministic IDs.
  Deterministic IDs are no longer supported by default.

- Applications no longer need to install `IdFeature` in order to generate IDs.

#### Image

- Introduction of `kairo-image` (some utilities for working with images).

#### Integration Testing

- **Simpler and faster integration testing.**
  - No need to spin up Ktor anymore — test the service layer directly.
  - Tests also run in parallel now!

- Integration tests now use JUnit extensions instead of inheritance.

#### Logging

- **No longer tightly coupled to Log4j2.**
  Choose your own SJF4J logging backend!
  - Recommended: **Stable Log4j 2** instead of beta Log4j 3.
  - Simplified recommended local log format (does not affect GCP logs).

- **Guidance to reduce noisy logs in production.**

#### Mailersend

- Introduction of `kairo-mailersend`,
  letting you easily send emails through [MailerSend](https://www.mailersend.com/).

#### Money

No changes.

#### Optional

- Introduced `Optional` differentiate between missing and null properties.

- Removed `Updater` and `update` in favor of `Optional`.

#### Protected String

- Minor changes to `toString()` result of `ProtectedString`.

#### Reflect

No changes.

#### REST

- **Easier REST definition & routing.**
  Some custom Kairo syntax has been reverted to Ktor native routing syntax
  plus optional DSL helpers.

- **Switch from CIO to Netty.**
  Netty's performance far exceeds CIOs in most situations,
  including with coroutines.
  Netty is also a far more popular library than CIO.
  _This change was actually made back in Kairo 5.0._

- Added support for **list query params.**

- The `@RestEndpoint.ContentType` and `@RestEndpoint.Accept` annotations are now optional.

- No more REST context class. Access Ktor's `RoutingCall` directly.

- Native Ktor SSE support.

- **Better error messages** when `RestEndpoint`s are malformed — easier debugging.

- **Colored call logging when running locally.**
  Instantly spot failures!

#### Serialization

- **Serialization with `kotlinx.serialization` (replaces Jackson).**
  Easier to use, fewer gotchas, and Kotlin-native.
  Fewer runtime surprises.

- **Automatic string trimming removed.**
  Data is now preserved exactly as sent.

#### Server

No changes.

#### Slack

- Now uses Slack's `AsyncMethodsClient` directly.

#### SQL

- **Type-safe SQL using [Exposed](https://www.jetbrains.com/exposed/)'s DSL (replaces JDBI).**
  - No more manual SQL strings,
    but retaining similar semantic alignment for predictability and easier debugging.

- R2DBC driver for async I/O (replaces JDBC).

- SQL health checks no longer run queries, avoiding DB log pollution.

- Custom type handling has been removed.

- Custom transaction management has been removed.

#### Testing

- Upgrade from Kotest 5 to Kotest 6.

- Test helper method descriptions are now optional.

#### Util

- Removal of `kairoEquals`, `kairoHashCode` and `kairoToString`.

- Addition of `canonicalize()` and `slugify()`.

- Addition of `firstCauseOf<T>()` for exceptions.

- Addition of the `resource()` Guava wrapper.

#### Removed libraries

- **Alternative Money Formatters**
- **Clock**
- **Closeable** (use built-in closeables instead).
- **Command Runner.**
  Connecting to GCP SQL instances that use [IAM Authentication](https://cloud.google.com/sql/docs/postgres/iam-authentication)
  is now supported through [kairo-sql-gcp](./kairo-sql/README.md#gcp)
- **Date Range**
- **Do Not Log String**
- **Environment Variable Supplier.**
  HOCON configs support native environment variable substitution.
- **Google Common**
- **Google Cloud Scheduler**
- **Google Cloud Tasks**
- **Hashing**
- **Lazy Supplier**
- **MDC**
- **Time**
- **Transaction Manager**
- **UUID** (use Kotlin's `Uuid` class directly instead).

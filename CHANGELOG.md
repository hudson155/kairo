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
  Easier to use, fewer gotchas, and Kotlin-native—faster onboarding.
  Fewer runtime surprises.

- **Dependency injection with [Koin](https://insert-koin.io/) (replaces Guice).**
  Reflection-free, Kotlin-friendly, better tooling, and simpler to configure.

- **Easier REST definition & routing.**
  TODO: Explain this further.

- **Type-safe SQL using [Exposed](https://www.jetbrains.com/exposed/)'s DSL (replaces JDBI).**
  - No more manual SQL strings,
    but retaining similar semantic alignment for predictability and easier debugging.
  - R2DBC driver for async I/O (replaces JDBC).

- **Switch to [HOCON](https://github.com/lightbend/config) for industry-standard configs.**
  - Great developer ergonomics.
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

#### Application

TODO: Coming soon!

#### Clock

TODO: Coming soon!

#### Config

TODO: Coming soon!

- **Switch to [HOCON](https://github.com/lightbend/config) for industry-standard configs.**
  - Built-in config inheritance and overrides for easy multi-env (dev/staging/prod).
  - Native environment variable substitution.

#### Coroutines

- `singleNullOrThrow()` now works with Kotlin `Flow`.

#### DARB

No changes.

#### Date Range

TODO: Coming soon!

#### Dependency Injection

TODO: Coming soon!

- **Dependency injection with [Koin](https://insert-koin.io/) (replaces Guice).**
  Reflection-free, Kotlin-friendly, better tooling, and simpler to configure.

#### Feature

TODO: Coming soon!

#### GCP Secret Supplier

- **GCP secrets are now fetched asynchronously on coroutines.**
  No blocking threads, and you can fetch multiple in parallel

- `FakeGcpSecretSupplier` for easier testing.

#### Google App Engine

- **Google App Engine is no longer supported.**
  For apps previously running on Google App Engine,
  consider using Google Cloud Run instead.

#### Google Cloud Scheduler

TODO: Coming soon!

#### Google Cloud Tasks

TODO: Coming soon!

#### Health Check

TODO: Coming soon!

- **Health checks now reflect actual readiness.**
  They won’t pass until Ktor can serve traffic.

#### ID

- **Safer IDs with zero runtime cost.**
  Kairo IDs like `user_ccU4Rn4DKVjCMqt3d0oAw3` now have compile-time safety,
  meaning you can't mix up a user ID and a business ID.
  This is done without runtime overhead.

- The valid entropy range has been expanded.

#### Logging

TODO: Coming soon!

- **Stable Log4j 2** instead of beta Log4j 3.

- Simplified local log format (does not affect GCP logs).

- **Guidance to reduce noisy logs in production.**

#### MDC

TODO: Coming soon!

#### Money

TODO: Coming soon!

#### Protected String

- Minor changes to `toString()` result of `ProtectedString`.

#### Reflect

No changes.

#### REST Client

TODO: Coming soon!

#### REST Feature

TODO: Coming soon!

- **Easier REST definition & routing.**
  TODO: Explain this further.

- **Switch from CIO to Netty.**
  Netty's performance far exceeds CIOs in most situations,
  including with coroutines.
  Netty is also a far more popular library than CIO.
  _This change was actually made back in Kairo 5.0._

- Added support for **list query params.**

- The `@RestEndpoint.ContentType` and `@RestEndpoint.Accept` annotations are now optional.

- **Better error messages** when `RestEndpoint`s are malformed — easier debugging.

- **Colored call logging when running locally.**
  Instantly spot failures!

#### Serialization

- **Serialization with `kotlinx.serialization` (replaces Jackson).**
  Easier to use, fewer gotchas, and Kotlin-native—faster onboarding.
  Fewer runtime surprises.

- **Automatic string trimming removed.**
  Data is now preserved exactly as sent.

#### Server

TODO: Coming soon!

#### SQL

TODO: Coming soon!

- **Type-safe SQL using [Exposed](https://www.jetbrains.com/exposed/)'s DSL (replaces JDBI).**
  - No more manual SQL strings,
    but retaining similar semantic alignment for predictability and easier debugging.
  - R2DBC driver for async I/O (replaces JDBC).

#### Testing

- Upgrade from Kotest 5 to Kotest 6.

- **Simpler and faster integration testing.**
  - No need to spin up Ktor anymore — test the service layer directly.
  - Tests also run in parallel now!

#### Updater

TODO: Coming soon!

#### Util

- Removal of `kairoEquals`, `kairoHashCode` and `kairoToString`.

#### UUID

TODO: Coming soon!

#### Removed libraries

- **Alternative Money Formatters**
- **Closeable** (use built-in closeables instead).
- **Command Runner**
  Connecting to GCP SQL instances that use [IAM Authentication](https://cloud.google.com/sql/docs/postgres/iam-authentication)
  is now supported through TODO: How is this supported?
- **Do Not Log String**
- **Environment Variable Supplier.**
  [HOCON](https://github.com/lightbend/config) configs support native environment variable substitution.
- **Exception**
- **Google Common**
- **Hashing**
- **Lazy Supplier**
- **Slack**
- **Time**
- **Transaction Manager**

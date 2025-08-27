# Kairo changelog

_Only non-bugfix release versions since Kairo 6.0 are included here._

## Kairo 6.0 - UNRELEASED

**Kairo 6.0** is a major release, with several breaking changes.

Kairo's philosophy has always been
to provide a collection designed to work seamlessly together
but flexible enough to slot into any project.
In previous versions of Kairo, however,
many of the libraries were far too rudimentary and/or tightly coupled to specific design decisions
to truly live up to this ideal.

Kairo 6 is a new era: Every library has been completely rewritten from the ground up,
and every library can now truly claim to be both independent and extensible.

**Several libraries have been simplified
and many others have been eliminated entirely,
in favor of external libraries that do the job far better.**

With Kairo 6,
there's also now a **bias toward Kotlin libraries instead of Java ones**.
Kairo was started in 2019.
At that time, Java was still the dominant language in the JVM ecosystem.
Kotlin libraries for everything from primitives like dates/times and UUIDs
to more advanced concepts like dependency injection
were either not yet available or not yet stable.
By 2025, the ecosystem has matured considerably.
With Kotlin now being the dominant language in the JVM ecosystem,
we've reconsidered the choice of many Java libraries.
We still use Java libraries where we deemed it most appropriate,
but we've also made a conscious effort to use Kotlin libraries wherever possible.
TODO: Offer examples? UUIDs, dates/times, Koin, etc.

### Kairo 6.0 highlights

- **Documentation improvements.**
  Kairo's documentation is now much more comprehensive,
  including examples and testing guidance for each library.

- **Introduction of BOMs (Bill of Materials).**
  If you're using one (or just a few Kairo libraries),
  we now recommend using `software.airborne.kairo:bom`
  to keep your Kairo dependencies aligned.
  If you're building a Kairo application,
  we recommend using the full bom (`software.airborne.kairo:bom-full`),
  which not only keeps your Kairo dependencies aligned
  but also aligns several external library versions.

- **Serialization now uses `kotlinx.serialization` instead of Jackson.**
  Although Jackson is a very comprehensive, production-ready serialization library,
  it's rather old and has a lot of baggage from earlier JVM days.
  Kotlin's own `kotlinx.serialization` is much more modern and has far fewer "gotchas".
  We think you'll find using it a pleasure.

- **Dependency injection now uses [Koin](https://insert-koin.io/) instead of Guice.**
  Koin is a Kotlin-native dependency injection framework
  that uses compiler plugins instead of runtime reflection.
  Similarly to Jackson, Guice is also rather old and has a lot of baggage from earlier JVM days.

- **New REST definition & routing syntax.**
  TODO: Explain this further.

- Introduction of **type-safe SQL DSL** using [Exposed](https://www.jetbrains.com/exposed/).
  This replaces JDBI — no more manually-written SQL queries!
  - Exposed also uses the R2DBC driver instead of JDBC, which supports async I/O.
  - One of the benefits of JDBI was that you got full control over the SQL,
    resulting in predictable and easy-to-debug queries.
    Although Exposed's idiomatic usage uses a DSL instead of raw SQL,
    there's very close alignment between the two.

- Kairo IDs like `user_ccU4Rn4DKVjCMqt3d0oAw3` now offer several distinct advantages.
  - **Semantic prefixes** (the "user" portion) for human-readability.
  - **Compile-time safety without runtime overhead.**
    Specifically, it's no longer possible to pass the wrong ID type,
    such as passing a business ID to a function that expects a user ID.
    This is checked at compile time, without runtime overhead.

- **The recommended integration testing approach no longer requires Ktor.**
  Instead of spinning up a Ktor server in order to run integration tests,
  they now test the service layer directly.

### Further changes

#### Alternative Money Formatters

This library has been removed.

#### Application

TODO: Coming soon!

#### Clock

TODO: Coming soon!

#### Closeable

This library has been removed.

Use built-in closeables instead.

#### Command Runner

This library has been removed.

Its primary use case was to provide a way to connect to GCP SQL instances
that used [IAM Authentication](https://cloud.google.com/sql/docs/postgres/iam-authentication).
TODO: Mention how to do this now instead.

#### Config

- **Configs now use [lightbend/config](https://github.com/lightbend/config)'s HOCON.**
  Kairo 5 had a homegrown config library built upon YAML,
  including some bespoke config inheritance functionality.
  Kairo 6 instead recommends HOCON.
  TODO: Add a link.

#### Coroutines

This library has been removed.

Use the Kotlin coroutines library directly instead.

#### DARB

No changes.

#### Date Range

TODO: Coming soon!

#### Dependency Injection

- **Dependency injection now uses [Koin](https://insert-koin.io/) instead of Guice.**
  Koin is a Kotlin-native dependency injection framework
  that uses compiler plugins instead of runtime reflection.

#### Do Not Log String

This library has been removed.

#### Environment Variable Supplier

This library has been removed.

Its primary use case was in configs,
but the new [kairo-config](./kairo-config) library
based on [lightbend/config](https://github.com/lightbend/config)
supports environment variables natively.

#### Exception

This library has been removed.

#### Feature

TODO: Coming soon!

#### GCP Secret Supplier

- **GCP secrets are now fetched on coroutines.**
  GCP Secret Manager has notoriously poor read performance,
  so secrets are now pulled on coroutines instead of blocking threads.
  Since Features also start concurrently now,
  secrets will also be fetched in parallel.
  This should help significantly improve application startup time.

- Introduction of `FakeGcpSecretSupplier`, which should help with testing.

#### Google App Engine

- **Google App Engine is no longer supported.**
  For apps previously running on Google App Engine,
  consider using Google Cloud Run instead.

#### Google Cloud Scheduler

TODO: Coming soon!

#### Google Cloud Tasks

TODO: Coming soon!

#### Google Common

This library has been removed.

#### Hashing

This library has been removed.

#### Health Check

TODO: Coming soon!

- In rare instances, the health readiness check used to succeed before Ktor was ready to take requests.
  **Health checks now fail until Ktor is ready.**

#### ID

- Kairo IDs like `user_ccU4Rn4DKVjCMqt3d0oAw3` now offer several distinct advantages.
  - **Semantic prefixes** (the "user" portion) for human-readability.
  - **Compile-time safety without runtime overhead.**
    Specifically, it's no longer possible to pass the wrong ID type,
    such as passing a business ID to a function that expects a user ID.
    This is checked at compile time, without runtime overhead.

#### Lazy Supplier

This library has been removed.

#### Logging

- **Downgrade from Log4j 3 to Log4j 2.**
  Log4j 3 is in beta, and is not widely used.

- Simplified local log format (does not affect GCP logs).

- **New recommendations to configure log levels for some Kairo libraries.**
  Some Kairo libraries can have rather noisy logs.
  We've added recommendations to configure log levels to avoid log pollution in your app.

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

- **New REST definition & routing syntax.**
  TODO: Explain this further.

- **Switch from CIO to Netty.**
  Netty's performance far exceeds CIOs in most situations,
  including with coroutines.
  Netty is also a far more popular library than CIO.
  _This change was actually made back in Kairo 5.0._

- Added support for **list query params.**

- The `@RestEndpoint.ContentType` and `@RestEndpoint.Accept` annotations are now optional.

- Better error messages when `RestEndpoint`s are malformed.

- **Colored call logging when running locally.**
  Colored call logging helps easily understand logs and identify failures locally.

#### Serialization

- **Serialization now uses `kotlinx.serialization` instead of Jackson.**
  Although Jackson is a very comprehensive, production-ready serialization library,
  it's rather old and has a lot of baggage from earlier JVM days.
  Kotlin's own `kotlinx.serialization` is much more modern and has far fewer "gotchas".
  We think you'll find using it a pleasure.

- **Removal of automatic string trimming.**
  Strings used to be trimmed (leading & trailing whitespace removed)
  automatically upon deserialization.
  This functionality has been removed.

#### Server

TODO: Coming soon!

#### Slack

This library has been removed.

#### SQL

TODO: Coming soon!

- Introduction of **type-safe SQL DSL** using [Exposed](https://www.jetbrains.com/exposed/).
  This replaces JDBI — no more manually-written SQL queries!
  - Exposed also uses the R2DBC driver instead of JDBC, which supports async I/O.
  - One of the benefits of JDBI was that you got full control over the SQL,
    resulting in predictable and easy-to-debug queries.
    Although Exposed's idiomatic usage uses a DSL instead of raw SQL,
    there's very close alignment between the two.

#### Testing

- Upgrade from Kotest 5 to Kotest 6.

- **The recommended integration testing approach no longer requires Ktor.**
  Instead of spinning up a Ktor server in order to run integration tests,
  they now test the service layer directly.

#### Time

This library has been removed.

#### Transaction Manager

This library has been removed.

#### Updater

TODO: Coming soon!

#### Util

- Removal of `kairoEquals`, `kairoHashCode` and `kairoToString`.

#### UUID

TODO: Coming soon!

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
and every library can now truly claim to be independent and extensible.

Several libraries have been simplified
and many others have been eliminated entirely,
in favor of external libraries that do the job far better.

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

- **Bias toward Kotlin libraries instead of Java ones.**
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

- **Serialization now uses `kotlinx.serialization` instead of Jackson.**
  Although Jackson is a very comprehensive, production-ready serialization library,
  it's rather old and has a lot of baggage from earlier JVM days.
  Kotlin's own `kotlinx.serialization` is much more modern and has far fewer "gotchas".
  We think you'll find using it a pleasure.

- **Dependency injection now uses [Koin](https://insert-koin.io/) instead of Guice.**
  Koin is a Kotlin-native dependency injection framework
  that uses compiler plugins instead of runtime reflection.
  - Kairo applications are also **no longer required to use dependency injection.**
    Kairo 5 applications were required to use Guice,
    since Kairo itself used and expected it internally.
    Kairo 6 does not use dependency injection internally anymore,
    and you don't need to use it either if you don't want to.
    Dependency injection is now a standalone Feature.

- Kairo IDs like `user_ccU4Rn4DKVjCMqt3d0oAw3` now offer several distinct advantages.
  - **Semantic prefixes** (the "user" portion) for human-readability.
  - **Compile-time safety without runtime overhead.**
    Specifically, it's no longer possible to pass the wrong ID type,
    such as passing a business ID to a function that expects a user ID.
    This is checked at compile time, without runtime overhead.

### Further changes

- **Configs now use [lightbend/config](https://github.com/lightbend/config)'s HOCON.**
  Kairo 5 had a homegrown config library built upon YAML,
  including some bespoke config inheritance functionality.
  Kairo 6 instead recommends HOCON.
  TODO: Add a link.

- **Google App Engine is no longer supported.**
  For apps previously running on Google App Engine,
  consider using Google Cloud Run instead.

- **Switch from CIO to Netty.**
  Netty's performance far exceeds CIOs in most situations,
  including with coroutines.
  Netty is also a far more popular library than CIO.
  _This change was actually made back in Kairo 5.0._

- **Colored call logging when running locally.**
  Colored call logging helps easily understand logs and identify failures locally.

- **Downgrade from Log4j 3 to Log4j 2.**
  Log4j 3 is in beta, and is not widely used.

- **Removal of `KairoType`.**
  Use Kotlin-native or Java-native solutions instead.

- **`kairo-command-runner` has been removed.**
  Its primary use case was to provide a way to connect to GCP SQL instances
  that used [IAM Authentication](https://cloud.google.com/sql/docs/postgres/iam-authentication).
  TODO: Mention how to do this now instead.

- **`kairo-do-not-log-string` has been removed.**

- **`kairo-environment-variable-supplier` has been removed.**
  Its primary use case was in configs,
  but the new [kairo-config](./kairo-config) library
  based on [lightbend/config](https://github.com/lightbend/config)
  supports environment variables natively.

- **GCP secrets are now fetched on coroutines.**
  GCP Secret Manager has notoriously poor read performance,
  so secrets are now pulled on coroutines instead of blocking threads.
  Since Features also start concurrently now,
  secrets will also be fetched in parallel.
  This should help significantly improve application startup time.

- **New recommendations to configure log levels for some Kairo libraries.**
  Some Kairo libraries can have rather noisy logs.
  We've added recommendations to configure log levels to avoid log pollution in your app.

- Removal of the Kairo transaction manager.

- Removal of `kairoEquals`, `kairoHashCode` and `kairoToString`.

- Upgrade from Kotest 5 to Kotest 6.

- Simplified local log format (does not affect GCP logs).

- Minor changes to `toString()` result of `ProtectedString`.

- Introduction of `FakeGcpSecretSupplier`, which should help with testing.

TODO: Any more removed libraries?

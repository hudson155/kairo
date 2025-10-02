# Kairo

**Kairo is your one-stop toolkit for production-ready Kotlin backends.**

Packed with a curated collection of proven, production-ready libraries,
it saves you the pain of hunting down, testing, and wiring together dependencies yourself.
Each library has been handcrafted for reliability, performance, and developer experience,
meaning you can skip the boilerplate and get straight to building something great.

Designed to work seamlessly together but flexible enough to slot into any project,
Kairo's libraries let you move faster, write cleaner code,
and focus on the parts of your app that actually matter.

## Getting started

_This section assumes you're using Gradle._

Kairo is hosted on Google Artifact Registry,
so you'll first need to add the Google Artifact Registry plugin
and connect to Airborne Software repository.

```kotlin
// build.gradle.kts

plugins {
  id("com.google.cloud.artifactregistry.gradle-plugin")
}

repositories {
  maven {
    url = uri("artifactregistry://us-central1-maven.pkg.dev/airborne-software/maven")
  }
}
```

### Using one (or just a few) Kairo libraries

Kairo's [standalone libraries](#standalone-libraries)
can be used independently within your existing project.

To use a single Kairo library,
you could just add it to your `dependencies` block as you normally would.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:YOUR-LIBRARY-OF-CHOICE:6.0.0")
}
```

However, we recommend using [Kairo's BOM](./bom/README.md)
to keep your Kairo dependencies aligned.

```kotlin
// build.gradle.kts

dependencies {
  implementation(platform("software.airborne.kairo:bom:6.0.0"))
  implementation("software.airborne.kairo:YOUR-LIBRARY-OF-CHOICE")
}
```

### Building a Kairo application

Great choice! You're in for a treat.

If you're building your entire application with Kairo,
we highly recommend using [Kairo's _full_ BOM](./bom-full/README.md),
which not only keeps your Kairo dependencies aligned
but also aligns several external library versions.

```kotlin
// build.gradle.kts

dependencies {
  implementation(platform("software.airborne.kairo:bom-full:6.0.0"))
}
```

## Libraries

### Standalone libraries

These libraries **can be used anywhere**
(within Kairo applications _or in any other Kotlin project_).

- [kairo-config](./kairo-config/README.md):
  HOCON-based configuration library
  with multi-env (dev/staging/prod) support, environment variable substitution,
  and dynamic config resolvers to pull properties from sources like Google Secret Manager.
- [kairo-coroutines](./kairo-coroutines/README.md):
  Extends Kotlin coroutines
  using [Arrow's coroutines library](https://arrow-kt.io/learn/coroutines/)
  and some convenient helper functions.
- [kairo-darb](./kairo-darb/README.md):
  Compact boolean list encoding for JWT permission lists,
  maintaining human-readability.
- [kairo-datetime](./kairo-datetime/README.md):
  Extends [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime)
  & adds some convenient helper functions.
- [kairo-exception](./kairo-exception/README.md):
  Differentiate between logical failures and real exceptions.
- [kairo-gcp-secret-supplier](./kairo-gcp-secret-supplier/README.md):
  Lightweight and coroutine-friendly
  Google Secret Manager wrapper.
- [kairo-id](./kairo-id/README.md):
  Human-readable semantic identifiers with variable entropy.
  Compile-time safety without runtime overhead.
- [kairo-image](./kairo-image/README.md):
  Some utilities for working with images.
- [kairo-logging](./kairo-logging/README.md):
  SLF4J-standardized logging (choose your own logging backend),
  exposing [Ohad Shai's Kotlin logging interface](https://github.com/oshai/kotlin-logging)
  for a clean, Kotlin-first API.
- [kairo-optional](./kairo-optional/README.md):
  Differentiate between missing and null values.
  This comes in especially handy for RFC 7396 (JSON Merge Patch).
- [kairo-protected-string](./kairo-protected-string/README.md):
  Handle sensitive strings that shouldn't be logged.
  `toString()` auto-redacts.
- [kairo-reflect](./kairo-reflect/README.md):
  Extends Kotlin reflection,
  unifying fragmented JVM/Kotlin into the safer and richer `KairoType<T>`.
- [kairo-serialization](./kairo-serialization/README.md):
  `kotlinx.serialization` "just works" — it's consistent and fast.
- [kairo-testing](./kairo-testing/README.md):
  Kotlin-esque JUnit-style testing, with coroutine and mocking support.
- [kairo-util](./kairo-util/README.md):
  A home for simple utilities.

### Application libraries

These libraries help when **building your full application with Kairo**.

- [kairo-application](./kairo-application/README.md):
  Start your Server,
  wait for JVM termination,
  and clean up afterwards,
  all with a single call.
- [kairo-client](./kairo-client/README.md):
  Ktor-native outgoing HTTP requests.
- [kairo-dependency-injection](./kairo-dependency-injection/README.md):
  Don't waste time and energy manually wiring your classes together.
  Let Koin create and wire everything instead!
- [kairo-feature](./kairo-feature/README.md):
  The core building block of Kairo applications,
  including both Framework Features and Domain Features.
- [kairo-health-check](./kairo-health-check/README.md):
  With just a few lines of code
  you can add health check endpoints to your Kairo application.
- [kairo-integration-testing](./kairo-integration-testing/README.md):
  Black-box integration testing support for Kairo applications.
- [kairo-server](./kairo-server/README.md):
  Servers are composed of Features,
  and can be run as an application or as part of integration tests.
- [kairo-sql](./kairo-sql/README.md):
  Standardizes SQL access in Kairo
  using [Exposed](https://www.jetbrains.com/exposed/)'s lightweight & idiomatic ORM DSL.

## Publishing

Publish a local snapshot.

```shell
./gradlew publishToMavenLocal -Pversion=9999-SNAPSHOT
# Or use the IntelliJ run configuration.
```

Publish a real version to Google Artifact Registry.

```shell
./gradlew --no-build-cache --no-configuration-cache publish -Pversion=0.0.0
```

## Code style

[Click here](https://github.com/hudson155/intellij-settings) for Kairo's IntelliJ code style.

## Style guide

Other than the rules defined here, please follow the
[Google Style Guide](https://developers.google.com/style).

- **Product terminology:**
  - Treat Kairo _Features_ and _Servers_ as proper nouns (the first letter should be capitalized).
  - Also use proper nouns for Kotlin _Flows_.

- **Sentence case:**
  Use American English style for general capitalization.
  Use sentence case in all headings, titles, and navigation.
  This includes for user-facing copy and within code and documentation.
  [This is consistent with the Google Style Guide](https://developers.google.com/style/text-formatting).

- **Full sentences:**
  Use full sentences, ending with proper punctuation.
  This applies to both copy and to code comments and logs.
  It also applies within bulleted and numbered lists.

- **Ordering:**
  - Order members in code by **read-create-update-delete** where possible.
  - Order lists alphabetically when there is no other natural order.
    This applies to both copy and to code (such as parameter/method lists and documentation).

- **Plurality:**
  For naming, singular forms should be preferred over plural forms where possible.
  This includes for folder, package, and Feature names.
  REST paths should be plural, due to convention.

- **Abbreviations:**
  In general, abbreviations should be avoided.
  Specifically, _i.e._ and _e.g._ should be avoided.

- **Non-null:**
  Prefer the term "non-null" to "not null".

- **Get vs. list vs. search:**
  The term "get" should be used when fetching a particular entity.
  The term "list" should be used when fetching all entities matching a simple condition.
  The term "search" should be used when fetching all entities matching some filter(s).

# Kairo

**Kairo is your one-stop toolkit for production-ready Kotlin backends.**

Packed with a curated collection of proven, production-ready libraries,
it saves you the pain of hunting down, testing, and wiring together dependencies yourself.
Each library has been handcrafted for reliability, performance, and developer experience,
meaning you can skip the boilerplate and get straight to building something great.

Designed to work seamlessly together but flexible enough to slot into any project,
Kairo's libraries let you move faster, write cleaner code,
and focus on the parts of your app that actually matter.

**Not sure where to start?**
The [kairo-sample](https://github.com/hudson155/kairo-sample) repo
showcases most of Kairo's functionality.

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

However, we recommend using [Kairo's BOM](./bom)
to keep your Kairo dependencies aligned.

```kotlin
// build.gradle.kts

dependencies {
  implementation(enforcedPlatform("software.airborne.kairo:bom:6.0.0"))
  implementation("software.airborne.kairo:YOUR-LIBRARY-OF-CHOICE")
}
```

### Building a Kairo application

Great choice! You're in for a treat.

If you're building your entire application with Kairo,
we highly recommend using [Kairo's _full_ BOM](./bom-full),
which not only keeps your Kairo dependencies aligned
but also aligns several external library versions.

```kotlin
// build.gradle.kts

dependencies {
  implementation(enforcedPlatform("software.airborne.kairo:bom-full:6.0.0"))
}
```

**Check out the [kairo-sample](https://github.com/hudson155/kairo-sample) repo**
to get an understanding of application structure.
You could even consider forking it to use as a template for your own app.

## Libraries

### Standalone libraries

These libraries **can be used anywhere**
(within Kairo applications _or in any other Kotlin project_).

- [kairo-config](./kairo-config):
  HOCON-based configuration library
  with multi-env (dev/staging/prod) support and environment variable substitution.
- [kairo-coroutines](./kairo-coroutines):
  Extends Kotlin coroutines
  using [Arrow's coroutines library](https://arrow-kt.io/learn/coroutines/)
  and some convenient helper functions.
- [kairo-darb](./kairo-darb):
  Compact boolean list encoding for JWT permission lists,
  maintaining human-readability.
- [kairo-gcp-secret-supplier](./kairo-gcp-secret-supplier):
  Lightweight and coroutine-friendly
  Google Secret Manager wrapper.
- [kairo-id](./kairo-id):
  Human-readable semantic identifiers with variable entropy.
  Compile-time safety without runtime overhead.
- [kairo-logging](./kairo-logging):
  SLF4J-standardized logging (choose your own logging backend),
  exposing [Ohad Shai's Kotlin logging interface](https://github.com/oshai/kotlin-logging)
  for a clean, Kotlin-first API.
- [kairo-protected-string](./kairo-protected-string):
  Handle sensitive strings that shouldn't be logged.
  `toString()` auto-redacts.
- [kairo-reflect](./kairo-reflect):
  Extends Kotlin reflection,
  unifying fragmented JVM/Kotlin into the safer and richer `KairoType<T>`.
- [kairo-testing](./kairo-testing):
  Kotlin-esque JUnit-style testing, with coroutine and mocking support.
- [kairo-util](./kairo-util):
  A home for simple utilities.

### Application libraries

These libraries help when **building your full application with Kairo**.

- [kairo-application](./kairo-application):
  Start your Server,
  wait for JVM termination,
  and clean up afterwards,
  all with a single call.
- [kairo-dependency-injection-feature](./kairo-dependency-injection/feature):
  Don't waste time and energy manually wiring your classes together.
  Let Koin create and wire everything instead!
- [kairo-feature](./kairo-feature):
  The core building block of Kairo applications,
  including both Framework Features and Domain Features.
- [kairo-health-check-feature](./kairo-health-check/feature):
  With just a few lines of code
  you can add health check endpoints to your Kairo application.
- [kairo-server](./kairo-server):
  Servers are composed of Features,
  and can be run as an application or as part of integration tests.

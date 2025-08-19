# Kairo

Kairo is your **one-stop toolkit** for efficiently building modern Kotlin applications.
Packed with a curated collection of proven, production-ready libraries,
it saves you the pain of hunting down, testing, and wiring together dependencies yourself.
Each library has been handcrafted for reliability, performance, and developer experience,
meaning you can skip the boilerplate and get straight to building something great.

Designed to work seamlessly together but flexible enough to slot into any project,
Kairo's libraries let you move faster, write cleaner code,
and focus on the parts of your app that actually matter.

TODO: Link to the sample repo.

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

TODO: Link to the sample repo.

## Libraries

### Standalone libraries

These libraries can be used within Kairo applications
or in any other Kotlin project.

- [kairo-config](./kairo-config):
  Robust and flexible HOCON-based configuration.
- [kairo-darb](./kairo-darb):
  DARB is a human-readable boolean list encoding
  whose primary use case is to cut down on JWT token size.
- [kairo-gcp-secret-supplier](./kairo-gcp-secret-supplier):
  Access GCP secrets from your Kotlin code (using coroutines).
- [kairo-id](./kairo-id):
  Human-readable semantic identifiers with variable entropy.
- [kairo-logging](./kairo-logging):
  Kotlin-idiomatic logging using [Ohad Shai's Kotlin logging interface](https://github.com/oshai/kotlin-logging).
- [kairo-protected-string](./kairo-protected-string):
  Handle strings that shouldn't be logged or carelessly exposed.
  `toString()` auto-redacts.
- [kairo-testing](./kairo-testing):
  Kotlin-esque JUnit-style testing, with coroutine and mocking support.
- [kairo-util](./kairo-util):
  A home for various simple utilities.

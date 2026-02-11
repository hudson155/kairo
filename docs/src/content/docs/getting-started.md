---
title: Getting started
---

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

## Using one (or just a few) Kairo libraries

Kairo's [standalone libraries](/modules/kairo-config/)
can be used independently within your existing project.

To use a single Kairo library,
you could just add it to your `dependencies` block as you normally would.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:YOUR-LIBRARY-OF-CHOICE:6.0.0")
}
```

However, we recommend using Kairo's BOM
to keep your Kairo dependencies aligned.

```kotlin
// build.gradle.kts

dependencies {
  implementation(platform("software.airborne.kairo:bom:6.0.0"))
  implementation("software.airborne.kairo:YOUR-LIBRARY-OF-CHOICE")
}
```

## Building a Kairo application

Great choice! You're in for a treat.

If you're building your entire application with Kairo,
we highly recommend using Kairo's _full_ BOM,
which not only keeps your Kairo dependencies aligned
but also aligns several external library versions.

```kotlin
// build.gradle.kts

dependencies {
  implementation(platform("software.airborne.kairo:bom-full:6.0.0"))
}
```

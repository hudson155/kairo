# Kairo Regular BOM

BOMs (Bill of Materials)
**keep dependency versions aligned and keep build files clean**.

Kairo has 2 BOMs (Bill of Materials).

- **This (`software.airborne.kairo:bom`) is the regular BOM**,
  intended for when you're **using one (or just a few) Kairo libraries**.
  It will keep your Kairo dependencies aligned.
- _If you're building a Kairo application,
  you should use [the full BOM](../bom-full) instead._

## Usage

Here's an example of what your `build.gradle.kts` file might look like
when using this BOM.

```kotlin
plugins {
  id("com.google.cloud.artifactregistry.gradle-plugin")
}

repositories {
  maven {
    // Kairo artifacts live here.
    url = uri("artifactregistry://us-central1-maven.pkg.dev/airborne-software/maven")
  }
}

dependencies {
  val kairoVersion = "6.0.0"
  implementation(enforcedPlatform("software.airborne.kairo:bom:$kairoVersion"))

  // Pull in whichever Kairo dependencies you need.
  implementation("software.airborne.kairo:kairo-coroutines")
  implementation("software.airborne.kairo:kairo-darb")
  implementation("software.airborne.kairo:kairo-protected-string")
  implementation("software.airborne.kairo:kairo-util")

  testImplementation("software.airborne.kairo:kairo-testing")
}
```

Notice how no versions are specified other than for the BOM?

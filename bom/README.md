# Kairo Regular BOM

Kairo has 2 BOMs (Bill of Materials).

**This (`software.airborne.kairo:bom`) is the regular BOM**,
intended for when you're **using one (or just a few) Kairo libraries**.
It will keep your Kairo dependencies aligned.

_If you're building a Kairo application,
you should use [the full BOM](../bom-full) instead._

See [the getting started section](..) for how to use this BOM.

## Example

Here's an example of what your `build.gradle.kts` file might look like
when using this BOM.

```kotlin
plugins {
  id("com.google.cloud.artifactregistry.gradle-plugin")
}

repositories {
  maven {
    url = uri("artifactregistry://us-central1-maven.pkg.dev/airborne-software/maven")
  }
}

dependencies {
  implementation(enforcedPlatform("software.airborne.kairo:bom:6.0.0"))

  implementation("software.airborne.kairo:kairo-darb")
  implementation("software.airborne.kairo:kairo-protected-string")
  implementation("software.airborne.kairo:kairo-util")

  testImplementation("software.airborne.kairo:kairo-testing")
}
```

Notice how no versions are specified other than for the BOM?

# Kairo Full BOM

BOMs (Bill of Materials)
**keep dependency versions aligned and keep build files clean**.

Kairo has 2 BOMs (Bill of Materials):

- **This (`software.airborne.kairo:bom`) is the full BOM**,
  intended for when you're **building a Kairo application**.
  It will not only keep your Kairo dependencies aligned,
  but also align several external library versions.
- _If you're just using one (or a few) Kairo libraries,
  you should use [the regular BOM](../bom) instead._

## External libraries

The following external libraries are included in this BOM:

- `io.arrow-kt:arrow-stack`
- `io.insert-koin:koin-bom`
- `io.insert-koin:koin-annotations-bom`
- `io.ktor:ktor-bom`
- `com.google.cloud:libraries-bom`
- `com.google.guava:guava-bom`
- `org.apache.logging.log4j:log4j-bom`
- `org.jetbrains.exposed:exposed-bom`
- `org.jetbrains.kotlinx:kotlinx-coroutines-bom`
- `org.jetbrains.kotlinx:kotlinx-serialization-bom`
- `org.slf4j:slf4j-bom`
- `org.testcontainers:testcontainers-bom`

## Usage

Here's an example of what your `build.gradle.kts` file might look like
when using this BOM.

```kotlin
plugins {
  kotlin("plugin.serialization")
  id("com.google.devtools.ksp")
  id("com.google.cloud.artifactregistry.gradle-plugin")
}

repositories {
  maven {
    url = uri("artifactregistry://us-central1-maven.pkg.dev/airborne-software/maven")
  }
}

dependencies {
  val kairoVersion = "6.0.0"
  ksp(enforcedPlatform("software.airborne.kairo:bom:$kairoVersion"))
  implementation(enforcedPlatform("software.airborne.kairo:bom:$kairoVersion"))

  ksp("io.insert-koin:koin-ksp-compiler")

  // Pull in whichever Kairo and external dependencies you need.
  implementation("com.google.guava:guava")
  implementation("io.insert-koin:koin-annotations")
  implementation("io.insert-koin:koin-core")
  runtimeOnly("org.apache.logging.log4j:log4j-core")
  runtimeOnly("org.apache.logging.log4j:log4j-slf4j2-impl")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
  implementation("software.airborne.kairo:kairo-application")
  implementation("software.airborne.kairo:kairo-config")
  implementation("software.airborne.kairo:kairo-coroutines")
  implementation("software.airborne.kairo:kairo-darb")
  implementation("software.airborne.kairo:kairo-dependency-injection-feature")
  implementation("software.airborne.kairo:kairo-gcp-secret-supplier")
  implementation("software.airborne.kairo:kairo-health-check-feature")
  implementation("software.airborne.kairo:kairo-logging")
  implementation("software.airborne.kairo:kairo-protected-string")
  implementation("software.airborne.kairo:kairo-reflect")
  implementation("software.airborne.kairo:kairo-rest-feature")
  implementation("software.airborne.kairo:kairo-sql-feature")
  implementation("software.airborne.kairo:kairo-util")

  testImplementation("software.airborne.kairo:kairo-testing")
}
```

Notice how no versions are specified other than for the BOM?

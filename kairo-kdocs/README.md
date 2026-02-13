# KDocs

Serves Dokka-generated API documentation as static resources from your Kairo application.
If no KDocs are found on the classpath, the feature is a no-op.

When used alongside [kairo-admin](../kairo-admin/README.md),
a KDocs link is automatically added to the admin dashboard sidebar.

## Installation

Install `kairo-kdocs`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-kdocs")
}
```

You also need the `kairo-service-dokka` convention plugin
applied to your application module to generate and package the docs.

```kotlin
// build.gradle.kts

plugins {
  id("kairo-service-dokka")
}
```

## Usage

Add the Feature to your Server.

```kotlin
val features = listOf(
  KdocsFeature(),
)
```

The docs are served at `/_kdocs` by default.

### Generating docs

Run the `packageKdocs` task to generate Dokka HTML and copy it to the classpath:

```shell
./gradlew :your-app:packageKdocs
```

This runs `dokkaGenerate` on the root project and syncs the output
into `build/resources/main/static/kdocs` so it's available at runtime.

### Configuration

```kotlin
KdocsConfig(
  pathPrefix = "/_kdocs",           // URL prefix for serving docs.
  resourcePath = "static/kdocs",    // Classpath location of the generated docs.
)
```

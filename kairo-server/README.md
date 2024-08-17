# `kairo-server`

A Server is an application that runs a set of Features.

Note: Treat Kairo _Servers_ as proper nouns (the first letter should be capitalized).

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  api("kairo:kairo-server:0.4.0")
}
```

### Step 2: Create your first Server

You can either run all of your Features on a single Server (monolithic architecture),
or spread them out across several Servers (microservices architecture).
This example is a monolith.

First, create the config class.
Regardless of whether you're building a monolith or microservices,
you'll need to configure some basic properties like `featureManager` and `restFeature`.

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServerConfig.kt

data class MonolithServerConfig(
  val featureManager: FeatureManagerConfig,
  val restFeature: RestConfig,
)
```

Finally, create the Server class itself.

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

class MonolithServer(
  config: MonolithServerConfig,
) : Server() {
  override val featureManager: FeatureManager =
    FeatureManager(
      features = setOf(
        RestFeature(config.restFeature),
        TaskFeature(),
      ),
      config = config.featureManager,
    )
}
```

# Stytch

Interface for [Stytch](https://stytch.com/),
letting you easily manage identity.

## Installation

Install `kairo-stytch-feature`.
You don't need to install the Stytch SDK separately —
it's included by default.

```kotlin
// build.gradle.kts

dependencies {
  implementation("com.highbeam.kairo:kairo-stytch-feature")
}
```

## Usage

First, add the Feature to your Server.

```kotlin
val features = listOf(
  StytchFeature(config.stytch),
)
```

We recommend using [kairo-config](../kairo-config/README.md) to configure the Feature.

```hocon
stytch {
  projectId = "project-live-00000000-abcd-1234-wxyz-000000000000"
  secret = ${STYTCH_SECRET}
}
```

Now you can interact with Stytch in your code.

```kotlin
val stytch: Stytch // Inject this.

stytch.users.create(...)
```

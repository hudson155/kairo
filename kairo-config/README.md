# Kairo Configs

## Overview

[lightbend/config](https://github.com/lightbend/config)'s HOCON implementation
is a robust and flexible configuration library.

```hocon
include "common.conf" # Include another config file.

app {
  port = 8080
}

sentryDsn = ${?SENTRY_DSN} # Optional environment variable.

databasePassword = ${DATABASE_PASSWORD} # Required environment variable.
```

## Installation

Get started by installing `kairo-config`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-config:6.0.0")
}
```

## Usage

TODO: Finish this README.

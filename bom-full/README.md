# Kairo Full BOM

The full Bill of Materials (BOM) keeps all Kairo library versions aligned,
plus the external libraries Kairo uses.

This is the recommended choice when building your entire application with Kairo.

## Installation

```kotlin
// build.gradle.kts

dependencies {
  implementation(platform("software.airborne.kairo:bom-full:6.0.0"))

  // Now you can add Kairo libraries without specifying versions.
  implementation("software.airborne.kairo:kairo-rest-feature")
  implementation("software.airborne.kairo:kairo-sql-feature")
}
```

## What's included

The full BOM includes version alignment for:

- Arrow
- Exposed
- GCP libraries
- Guava
- Jackson
- Koin
- Kotest
- kotlinx-coroutines
- kotlinx-datetime
- Ktor
- Log4j
- MailerSend SDK
- MockK
- Moneta
- PostgreSQL (JDBC and R2DBC drivers)
- R2DBC Pool
- Slack SDK
- SLF4J
- Stytch SDK
- Testcontainers

## When to use the regular BOM

If you're only using a few Kairo libraries within an existing project,
use the [regular BOM](../bom/README.md) instead.
This avoids potential conflicts with your existing dependency versions.

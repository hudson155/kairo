# Kairo Date/Time

`kairo-datetime` provides [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime)
and adds some **convenient helper functions**.

## Installation

Install `kairo-datetime`.
You don't need to install Kotlin's datetime library separately —
it's included by default.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-datetime")
}
```

## Usage

See [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime).

- `Instant` (standard library)
- `LocalDateTime`, `LocalDate`, `YearMonth`, `LocalTime`
- `TimeZone`
- `Month` and `DayOfWeek` enums

### Helper functions

#### `Instant.Companion.epoch`

A convenient way to get the Unix epoch instant.

```kotlin
Instant.epoch
// => 1970-01-01T00:00:00Z
```

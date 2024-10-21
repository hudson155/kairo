# `kairo-date range`

Library for date range types:

- `InstantRange`
- `LocalDateRange`
- `YearMonthRange`

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-date-range:$kairoVersion")
}
```

### Step 2: Use date ranges

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

LocalDateRange.inclusive(
  start = LocalDate.parse("2023-11-13"),
  endInclusive = LocalDate.parse("2024-01-04"),
)
```

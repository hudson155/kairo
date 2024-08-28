# `kairo-updater`

A utility library for updating existing entities
that supports `Optional`s to differentiate between `null` and `undefined` from the frontend.

Used by [kairo-sql-feature](/kairo-sql-feature).

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-updater:$kairoVersion")
}
```

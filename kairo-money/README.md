# `kairo-money`

The `Money` type is provided by JSR-354.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-money:$kairoVersion")
}
```

### Step 2: Use the money type

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

val amount = Money.of(123.45, "USD")
```

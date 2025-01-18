# `kairo-hashing`

Kairo's hashing library currently only supports simple MD5.

Note: Do not attempt to use this for encryption. It is not secure.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-hashing:$kairoVersion")
}
```

### Step 2: Hash

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

"My string.".md5()
```

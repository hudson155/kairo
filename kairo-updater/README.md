# `kairo-updater`

**Refer to the [kairo-sample](https://github.com/hudson155/kairo-sample) repository to see it in action.**

A utility library for updating existing entities
that supports `Optional`s to differentiate between `null` and `undefined` from the frontend.

Used by [kairo-sql-feature](../kairo-sql-feature).

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-updater:$kairoVersion")
}
```

### Step 2: Use it

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

Updater { existing ->
  LibraryBookModel.Update(
    title = update(existing.title, update.title),
    author = update(existing.author, update.author),
  )
}
```

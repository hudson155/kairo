# `kairo-lazy-supplier`

Simple type-wrapping class that allows values to be provided and supplied lazily and asynchronously.
Values will not be fetched more than once.
If you need something more advanced, consider [Caffeine](https://github.com/ben-manes/caffeine)
with [Aedile](https://github.com/sksamuel/aedile).

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-lazy-supplier:$kairoVersion")
}
```

### Step 2: Use the lazy supplier

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

val supplier = LazySupplier { doSomethingSlow() }
supplier.get() // The first call will be slow.
supplier.get() // Subsequent calls will be fast.
```

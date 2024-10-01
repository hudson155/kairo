# `kairo-gcp-secret-supplier`

`GcpSecretSupplier` supplies GCP secrets.
It delegates to the GCP Secret Manager SDK,
but uses an abstract class for testability.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  api("kairo:kairo-gcp-secret-supplier:$kairoVersion")
}
```

### Step 2: Get a GCP secret

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

val gcpSecretSupplier: GcpSecretSupplier = DefaultGcpSecretSupplier
gcpSecretSupplier["projects/012345678900/secrets/example/versions/1"]
```

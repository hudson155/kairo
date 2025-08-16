# GCP Secret Supplier

This library provides access to
[Google Cloud Platform (GCP) secrets](https://cloud.google.com/security/products/secret-manager).
It's a thin wrapper around Google's own Java SDK,
primarily with the intention of making it usable in a Kotlin coroutine environment.

```kotlin
gcpSecretSupplier["projects/012345678900/secrets/example/versions/1"]
```

## Installation

Get started by installing `kairo-gcp-secret-manager`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-gcp-secret-manager:6.0.0")
  testImplementation("software.airborne.kairo:kairo-gcp-secret-manager-testing:6.0.0")
}
```

## Usage

First, create an instance of `GcpSecretSupplier`.

```kotlin
val gcpSecretSupplier: GcpSecretSupplier = DefaultGcpSecretSupplier()
```

Now use it simply by using the get operator.

```kotlin
val value = gcpSecretSupplier["projects/012345678900/secrets/example/versions/1"]
```

The result is either the value of the GCP secret, or `null` if the GCP secret does not exist.

## Testing

There are a few `GcpSecretSupplier` implementations that might come in handy for testing.

- `NoopGcpSecretSupplier` will throw `NotImplementedError` if you try to access a GCP secret.
- `FakeGcpSecretSupplier` allows you to set GCP secret values manually.

You can also implement your own custom `GcpSecretSupplier`.

## Logging config

We recommend excluding logs below the `INFO` level for this library.

```xml
<Logger name="kairo.gcpSecretSupplier" level="info"/>
```

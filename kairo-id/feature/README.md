# Kairo ID Feature

Kairo IDs are **safe, meaningful, and efficient**.
`kairo-id` is an alternative to raw UUIDs or serial IDs,
improving **developer experience** and **operational clarity**.

### Why Kairo IDs?

- **Semantic prefixes:** IDs tell you what they represent (`user_123`, `business_123`).
- **Strong entropy:** As much or more randomness than UUIDs, tunable by payload length.
- **Compile-time safety:** No more accidentally swapping IDs of different entity types.
- **Zero runtime overhead:** Powered by Kotlin value classes (inlined to strings).

#### Example

An example Kairo ID is `user_ccU4Rn4DKVjCMqt3d0oAw3`.

- **Prefix:** `user` (human-readable entity type).
- **Payload:** `ccU4Rn4DKVjCMqt3d0oAw3` (base-62 encoded randomness).

## Installation

Get started by installing `kairo-id-feature`.\
You don't need to install `kairo-id` separately — it's included.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-id-feature")
}
```

## Usage

Add the Feature to your Server.

```kotlin
Server(
  features = listOf(
    IdFeature(),
  ),
)
```

That's enough! Now you're ready to generate some IDs!

```kotlin
@Single
class UserIdGenerator(
  strategy: IdGenerationStrategy,
) : IdGenerator<UserId>(strategy, prefix = "user") {
  override fun generate(value: String): UserId =
    UserId(value)
}
```

### Configuring the ID generator

By default, ID payloads are **15 characters**.
You might want to tweak this
after reviewing [the entropy and length guidance](../README.md#entropy-and-length-guidance).

To change the length, pass in your desired length.

```kotlin
IdFeature(
  config = IdFeatureConfig(
    generation = IdFeatureConfig.Generation.Random(length = 22),
  ),
)
```

Or use [Kairo config](../../kairo-config)'s HOCON.

```hocon
id.generation {
  type = "Random"
  length = 22
}
```

### Testing

For testing, use the deterministic ID generation strategy

```kotlin
IdFeature(
  config = IdFeatureConfig(
    generation = IdFeatureConfig.Generation.Deterministic(length = 22),
  ),
)
```

Or use [Kairo config](../../kairo-config)'s HOCON.

```hocon
id.generation {
  type = "Deterministic"
  length = 22
}
```

# Kairo ID Feature

Binds [Kairo ID generation](..) for use within a Kairo application.

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

Or using [Kairo config](../../kairo-config)'s HOCON.

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

Or using [Kairo config](../../kairo-config)'s HOCON.

```hocon
id.generation {
  type = "Deterministic"
  length = 22
}
```

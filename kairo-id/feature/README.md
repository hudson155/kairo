# Kairo ID Feature

Kairo IDs are an optional way of managing unique identifiers.
Think of a Kairo ID as an alternative to a `UUID` or serial ID, but with a few advantages.

- Semantic prefixes for human-readability.
- Variable entropy, including as much or more than UUIDs.
- Compile-time safety without runtime overhead.

An example Kairo ID is `user_ccU4Rn4DKVjCMqt3d0oAw3`.

See [the ID library](..) for more information about Kairo IDs.
This Feature binds Kairo ID generation for use within a Kairo application.

## Installation

Get started by installing `kairo-id-feature`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-id-feature:6.0.0")
}
```

## Usage

The most simple usage is just to add the Feature on its own.

```kotlin
IdFeature()
```

That's enough! Now you're ready to generate some IDs!

```kotlin
@Single(createdAtStart = true)
class UserIdGenerator(
  strategy: IdGenerationStrategy,
) : IdGenerator<UserId>(strategy, prefix = "user") {
  override fun generate(value: String): UserId =
    UserId(value)
}
```

### Configuring the ID generator

By default, ID payloads will be 15 characters.
You might want to tweak this
after reviewing [the entropy and length guide](../README.md#entropy-and-length-guidance).

To change the length, pass in your desired length.

```kotlin
IdFeature(
  config = IdFeatureConfig(
    generation = IdFeatureConfig.Generation.Random(length = 22),
  ),
)
```

If you're using [Kairo configs](../../kairo-config),
your HOCON will look like this.

```hocon
id {
  generation {
    type = "Random"
    length = 22
  }
}
```

TODO: Add testing instructions.

# Kairo IDs

Kairo IDs are **safe, meaningful, and efficient**.
`kairo-id` is an alternative to raw UUIDs or serial IDs,
improving **developer experience** and **operational clarity**.

See [kairo-id-feature](./feature)
to easily add Kairo ID generation to your Kairo application.

### Why Kairo IDs?

- **Semantic prefixes:** IDs tell you what they represent (`user_123`, `business_123`).
- **Strong entropy:** As much or more randomness than UUIDs, tunable by payload length.
- **Compile-time safety:** No more accidentally swapping IDs of different entity types.
- **Zero runtime overhead:** Powered by Kotlin value classes (inlined to strings).

#### Example

An example Kairo ID is `user_ccU4Rn4DKVjCMqt3d0oAw3`.

- **Prefix:** `user` (human-readable entity type).
- **Payload:** `ccU4Rn4DKVjCMqt3d0oAw3` (base-62 encoded randomness).

### Entropy and length guidance

The entropy of Kairo IDs depends on the length of the payload portion.

| ID type                 | Entropy                 |
|-------------------------|-------------------------|
| Kairo ID, length 5      | 29.8 bits (equivalent)  |
| Integer (positive only) | 32 bits                 |
| Kairo ID, length 8      | 47.6 bits (equivalent)  |
| Long (positive only)    | 64 bits                 |
| Kairo ID, length 15     | 89.3 bits (equivalent)  |
| UUID (version 4)        | 122 bits                |
| Kairo ID, length 22     | 131.0 bits (equivalent) |
| Kairo ID, length 32     | 190.5 bits (equivalent) |

_Entropy calculation: `length * log2(62)`._

- **22:** Slightly higher entropy than UUIDs.
- **15:** Good balance of entropy and readability.
- **5-8:** Only if a small keyspace is acceptable.

_Note: like any ID scheme, Kairo IDs involve tradeoffs —
generation cost, DB storage size, and index performance.
Use Kairo IDs when human clarity and safety outweigh those tradeoffs._

## Installation

Get started by installing `kairo-id`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-id")
}
```

## Usage

### Define ID types

Each entity should define its own ID type — you can't use `Id` directly.

```kotlin
@Serializable
@JvmInline
value class UserId(override val value: String) : Id {
  init {
    require(regex.matches(value)) { "Malformed user ID (value=$value)." }
  }

  companion object {
    val regex: Regex = Id.regex(prefix = Regex("user"))
  }
}
```

This enforces **compile-time safety**: IDs can't be mixed up.
Without semantic IDs, callers could accidentally swap `userId` and `businessId`.
With Kairo IDs, the compiler prevents this mistake.

```kotlin
fun listRoles(businessId: BusinessId, userId: UserId): List<Role> {
  // ...
}
```

### Generate IDs

Each ID type also needs a generator.

```kotlin
class UserIdGenerator(
  strategy: IdGenerationStrategy,
) : IdGenerator<UserId>(strategy, prefix = "user") {
  override fun generate(value: String): UserId =
    UserId(value)
}
```

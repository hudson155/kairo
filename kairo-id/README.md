# Kairo IDs

Kairo IDs are an optional way of managing unique identifiers.
Think of a Kairo ID as an alternative to a `UUID` or serial ID, but with a few advantages.

- Semantic prefixes for human-readability.
- Variable entropy, including as much or more than UUIDs.
- Compile-time safety without runtime overhead.

An example Kairo ID is `user_ccU4Rn4DKVjCMqt3d0oAw3`.
The "semantic" part means that a human can easily understand that this is a _user_ ID
rather than the ID for a different entity.
This is mostly useful for developers, but it can also make URL slugs look nicer and stuff.

Kairo IDs consist of a "prefix" portion and a "payload" portion.
In the example above, `user_ccU4Rn4DKVjCMqt3d0oAw3` has a prefix of `user`
and a payload of `ccU4Rn4DKVjCMqt3d0oAw3`.

- The **prefix** indicates what the entity is for.
  Its value is totally up to you, but needs to be snake case.
- The **payload** is a base-62 string.
  Its length is up to you; **22 is a good default**.

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

#### Length guidance

- 22 is a good default, since it has similar (slightly higher) entropy to UUIDs.
- If you want to prioritize readability and don't need UUID-level entropy,
  consider a length of 15.
- Only use smaller lengths like 5 or 8 if you're confident that a small keyspace is acceptable.

_Note: Kairo IDs aren't a free lunch.
There are plenty of performance tradeoffs,
including ID generation cost, database storage size & index performance, etc.
The benefits of better developer experience and readability
can be outweighed by performance tradeoffs for some use cases._

## Installation

Get started by installing `kairo-id`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-id")
}
```

## Usage

### Defining ID types

You can't use `Id` directly.
Instead, each ID type must be uniquely defined.

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

This extra boilerplate enforces compile-time safety
by ensuring that IDs can't be mixed up by accident.

For example, say you have a function like this:

```kotlin
fun listRoles(businessId: BusinessId, userId: UserId): List<Role> {
  // ...
}
```

Without semantic IDs, the caller could accidentally swap the parameters:

```kotlin
listRoles(userId, businessId)
```

This mistake wouldn't be caught at compile-time.
_With_ semantic IDs, the compiler will catch this error!

#### Runtime performance

Since ID types are defined as value classes (Java inline classes),
the compiler inlines them to be strings at runtime.
This means the compile-time safety doesn't come at the cost of runtime performance!

### Generating IDs

For each ID type, you must also define an ID generator.

```kotlin
class UserIdGenerator(
  strategy: IdGenerationStrategy,
) : IdGenerator<UserId>(strategy, prefix = "user") {
  override fun generate(value: String): UserId =
    UserId(value)
}
```

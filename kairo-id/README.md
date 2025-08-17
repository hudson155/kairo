# Kairo IDs

Kairo has a class you can optionally use to represent _semantic identifiers_ for different entities.
Think of a Kairo ID as an alternative to a `UUID` or serial ID, but with a few fun perks.

An example Kairo ID is `user_ccU4Rn4DKVjCMqt3d0oAw3`.
The "semantic" part means that a human can easily understand this is a user's ID
rather than the ID for a different entity.
This is mostly useful for developers, but it can also make URL slugs look nicer for users.

Kairo IDs consist of a "prefix" portion and a "value" portion.
In the example above, `user_ccU4Rn4DKVjCMqt3d0oAw3` has a prefix of `user`
and a value of `ccU4Rn4DKVjCMqt3d0oAw3`.

- The **prefix** indicates what the entity is for.
  The value is totally up to you, but needs to be snake case.
- The **value** is a base-62 string.
  The length is up to you, but 22 is a good default.

The entropy of Kairo IDs depends on the length of the value portion.

| ID type                 | Entropy                 |
|-------------------------|-------------------------|
| Integer (positive only) | 32 bits                 |
| Kairo ID, length 8      | 47.6 bits (equivalent)  |
| Long (positive only)    | 64 bits                 |
| Kairo ID, length 15     | 89.3 bits (equivalent)  |
| UUID (version 4)        | 122 bits                |
| Kairo ID, length 22     | 131.0 bits (equivalent) |

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
  implementation("software.airborne.kairo:kairo-id:6.0.0")
}
```

## Usage

TODO: Write usage guide.

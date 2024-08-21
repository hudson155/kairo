# `kairo-id`

Kairo IDs (`KairoId`s) are an optional way to uniquely identify entities.
Think of them as an alternative to `UUID`s or serial IDs, but with a few perks.
Kairo IDs consist of a prefix portion and an ID portion.
The prefix portion is context-specific,
and the ID portion is a base-62 string.

**An example:**

An example Kairo ID for a library book would look like `library_book_ccU4Rn4DKVjCMqt3d0oAw3`.
The prefix portion here is `library_book`,
and the ID portion is `ccU4Rn4DKVjCMqt3d0oAw3`.

**Entropy:**

UUIDs are the gold standard for high-entropy IDs.
There are `16^32 = 3.4E+38` distinct UUIDs.

Kairo IDs can vary in entropy depending on how you configure them.
- Using the minimum length of 8 provides `62^8 = 2.2E+14` distinct IDs.
  This is fairly low, but could be reasonable for use cases where readability is preferred.
- **Using a length of 22** provides `62^22 = 2.7E+39` distinct IDs,
  which is just slightly more than that of UUIDs.
- Using the maximum length of 32 provides `62^32 = 2.3E+57` distinct IDs.
  This is very high.

**Features:**

- **Kairo IDs are contextual**,
  since they specify what entity they're for using their prefix.
  This is quite nice for humans passing around IDs out-of-band.
- As mentioned above, entropy can be high or low depending on the length configuration.
  Using a length of 22 provides just slightly more entropy than that of UUIDs.
- Underscores are used so that an ID can be **double-clicked and easily copied**.
  This does not work with hyphens.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  testImplementation("kairo:kairo-id:0.4.0")
}
```

### Step 2: Create and configure the Feature

```yaml
# src/main/resources/config/config.yaml

id:
  generator:
    type: "Random"
    length: 22
```

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

KairoIdFeature(config.id)
```

### Step 3: Generate IDs

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

class MyClass @Inject constructor(
  idGenerator: IdGenerator.Factory,
) {
  private val idGenerator: IdGenerator =
    idGenerator.withPrefix("library_book")

  fun myMethod() {
    idGenerator.generate()
  }
}
```

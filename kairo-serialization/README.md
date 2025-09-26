# Kairo Serialization

`kotlinx.serialization` "just works" — it's **consistent and fast**.

## Installation

Install `kairo-serialization` and the serialization plugin.\
You don't need to install `kotlinx-serialization-core` separately — it's included by default.
But you should install `kotlinx-serialization-json` or other format-specific libraries if you need them.

```kotlin
// build.gradle.kts

plugins {
  kotlin("plugin.serialization")
}

dependencies {
  implementation("software.airborne.kairo:kairo-serialization")
}
```

## Usage

### Basic usage

```kotlin
@Serializable
data class MyClass(
  val number: Int,
  val text: String,
)

val json: Json = 
  Json {
    kairo()
  }

json.encodeToString(MyClass(1, "foo"))
// => {"number":1,"text":"foo"}

json.decodeFromString<MyClass>("""{"number":1,"text":"foo"}""")
// => MyClass(number=1, text=foo)
```

### Polymorphic serialization

It's pretty common to need to serialize/deserialize polymorphic types.
`kotlinx.serialization` is opinionated on how to do this.
Use sealed classes if you can.

```kotlin
@Serializable
sealed class Animal {
  abstract val name: String

  @Serializable
  @SerialName("Dog")
  data class Dog(override val name: String, val barksPerMinute: Int) : Animal()
  
  @Serializable
  @SerialName("Cat")
  data class Cat(override val name: String, val napsPerDay: Int) : Animal()
}

val json: Json = 
  Json {
    kairo()
  }

json.encodeToString(listOf(Animal.Dog("Rex", 30), Animal.Cat("Whiskers", 12)))
// => [{"type":"Dog","name":"Rex","barksPerMinute":30},{"type":"Cat","name":"Whiskers","napsPerDay":12}]

json.decodeFromString<List<Animal>>(
  """[
    {"type":"Dog","name":"Rex","barksPerMinute":30},
    {"type":"Cat","name":"Whiskers","napsPerDay":12}
  ]""",
)
// => [Animal.Dog(name=Rex, barksPerMinute=30), Animal.Cat(name=Whiskers, napsPerDay=12)]
```

### Built-in serializers

There are built-in serializers for a few types, but you need to reference them manually.

#### `BigDecimal`

You can either serialize to a double or to a string.
It's better to serialize to a string, because doubles have a maximum precision.

```kotlin
@Serializable
data class MyClass(
  @Serializable(with = BigDecimalSerializer.AsString::class)
  val value: BigDecimal,
)

json.encodeToString(MyClass(BigDecimal("123.456")))
// => {"value":"123.456"}
```

#### `BigInteger`

You can either serialize to a long or to a string.
It's better to serialize to a string, because longs have a maximum range.

```kotlin
@Serializable
data class MyClass(
  @Serializable(with = BigIntegerSerializer.AsString::class)
  val value: BigInteger,
)

json.encodeToString(MyClass(BigInteger("12345")))
// => {"value":"12345"}
```

### Advanced usage

This README only covers the basics.
Refer to `kotlinx.serialization`'s documentation for advanced usage.

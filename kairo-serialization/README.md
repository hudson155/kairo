# Kairo Serialization

Kairo uses [Jackson](https://github.com/FasterXML/jackson) for serialization.
`kotlinx.serialization` is a good library, but it's not as mature and robust as Jackson.

## Installation

Install `kairo-serialization` and the serialization plugin.\
You don't need to install Jackson separately —
it's included by default.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-serialization")
}
```

## Usage

### Basic usage

```kotlin
data class MyClass(
  val number: Int,
  val text: String,
)

val jsonMapper: JsonMapper = kairoJson()

jsonMapper.writeValueAsString(MyClass(1, "foo"))
// => {"number":1,"text":"foo"}

Json.readValue<MyClass>("""{"number":1,"text":"foo"}""")
// => MyClass(number=1, text=foo)
```

### Polymorphic serialization

It's pretty common to need to serialize/deserialize polymorphic types.
Jackson supports this well. Use sealed classes if you can.

```kotlin
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(Dog::class, name = "Dog"),
  JsonSubTypes.Type(Cat::class, name = "Cat"),
)
sealed class Animal {
  abstract val name: String

  data class Dog(override val name: String, val barksPerMinute: Int) : Animal()

  data class Cat(override val name: String, val napsPerDay: Int) : Animal()
}

jsonMapper.writeValueAsString(listOf(Animal.Dog("Rex", 30), Animal.Cat("Whiskers", 12)))
// => [{"type":"Dog","name":"Rex","barksPerMinute":30},{"type":"Cat","name":"Whiskers","napsPerDay":12}]

jsonMapper.readValue<List<Animal>>(
  """
  [
    {"type":"Dog","name":"Rex","barksPerMinute":30},
    {"type":"Cat","name":"Whiskers","napsPerDay":12}
  ]
  """,
)
// => [Animal.Dog(name=Rex, barksPerMinute=30), Animal.Cat(name=Whiskers, napsPerDay=12)]
```

### Well-known types

The following types are considered "well-known",
meaning that Kairo is known to support them well,
and has exploratory testing for them which ensures release-to-release stability.

#### Numeric types

- `BigDecimal`
- `BigInteger`
- `Byte` and `UByte`
- `Double`
- `Float`
- `Int` and `UInt`
- `Long` and `ULong`
- `Short` and `UShort`

#### String and string-like

- `CharArray`
- `Char`
- `String`

#### Time-related

- `DayOfWeek`
- `Duration`
- `Instant`
- `LocalDateTime`
- `LocalDate`
- `LocalTime`
- `MonthDay`
- `Month`
- `OffsetDateTime`
- `OffsetTime`
- `Period`
- `YearMonth`
- `Year`
- `ZonedDateTime`
- `ZoneId`
- `ZoneOffset`

#### Other

- `Boolean`
- Enums
- `Unit`

### Advanced usage

This README only covers the basics.
Refer to Jackson's documentation for advanced usage.

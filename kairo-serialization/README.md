# Kairo Serialization

Kairo provides a wrapper around [Jackson](https://github.com/FasterXML/jackson)
for serialization.

### Why not `kotlinx.serialization`?

`kotlinx.serialization` is a very good library.
It works very well with Ktor, and excels on Multiplatform.
However, it's not yet as robust as Jackson in several ways.

- One of these key ways is that Jackson's exceptions are very rich:
  They include the exact problem details, the JSON path to the offending field, and even the source location.
  `kotlinx.serialization` doesn't have anywhere near this level of detail in its exceptions.
  Jackson's level of exception detail allows Kairo to convert these failures into user-friendly API errors.

- Another place Jackson shows its maturity is with polymorphic serialization.
  `kotlinx.serialization` supports this in a good but limited way;
  Jackson has robust support for configurable type ID resolution, ensuring compatibility with third-party APIs.

## Jackson

That said, being such an old library, Jackson has its fair share of baggage.

- Jackson has a lot of poor defaults around type leniency, property naming, etc.
  These defaults are in place mostly for backwards-compatibility, but they can lead to surprising results.
  **Kairo replaces these defaults with much stricter and less surprising ones.**
  You can still override Kairo's defaults the same way you would for Jackson.

- Another issue with Jackson is that it loses type information during runtime serialization.
  This can lead to surprises
  like polymorphic type information not being included when serializing the type directly
  (rather than as a property).
  **Kairo fixes this issue by leveraging Kotlin type inference
  through [kairo-reflect](../kairo-reflect/README.md).**

- Finally, since Jackson is a Java library rather than a Kotlin one,
  it doesn't have native support for Kotlin's nullability guarantees.
  **The Kairo wrapper supports Kotlin's nullability guarantees.**

In order to fix the problems mentioned above,
`kairo-serialization` provides a wrapper around Jackson.

## Installation

Install `kairo-serialization`.
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

val json: KairoJson = KairoJson()

json.serialize(MyClass(1, "foo"))
// => {"number":1,"text":"foo"}

json.deserialize<MyClass>("""{"number":1,"text":"foo"}""")
// => MyClass(number=1, text=foo)
```

### Polymorphic serialization

It's pretty common to need to serialize/deserialize polymorphic types.
Jackson supports this well. Use sealed classes if you can.

```kotlin
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(Animal.Dog::class, name = "Dog"),
  JsonSubTypes.Type(Animal.Cat::class, name = "Cat"),
)
sealed class Animal {
  abstract val name: String

  data class Dog(override val name: String, val barksPerMinute: Int) : Animal()

  data class Cat(override val name: String, val napsPerDay: Int) : Animal()
}

json.serialize(
  listOf(Animal.Dog("Rex", 30), Animal.Cat("Whiskers", 12)),
)
// => [{"type":"Dog","name":"Rex","barksPerMinute":30},{"type":"Cat","name":"Whiskers","napsPerDay":12}]

json.deserialize<List<Animal>>(
  "["type":"Dog","name":"Rex","barksPerMinute":30},{"type":"Cat","name":"Whiskers","napsPerDay":12}]",
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

- `DayOfWeek` (Java and Kotlin)
- `Duration` (Java and Kotlin)
- `Instant` (Java and Kotlin)
- `LocalDateTime` (Java and Kotlin)
- `LocalDate` (Java and Kotlin)
- `LocalTime` (Java and Kotlin)
- `MonthDay` (Java-only)
- `Month` (Java and Kotlin)
- `OffsetDateTime` (Java-only)
- `OffsetTime` (Java-only)
- `Period` (Java) and `DatePeriod` (Kotlin)
- `YearMonth` (Java and Kotlin)
- `Year` (Java-only)
- `ZonedDateTime` (Java-only)
- `ZoneId` (Java) and `TimeZone` (Kotlin)
- `ZoneOffset` (Java) and `FixedOffsetTimeZone` (Kotlin)

#### Collections

- `List<E>`
- `Set<E>`
- `Map<K, V>`

#### Other

- `Boolean`
- Enums
- Kotlin data classes and data objects,
  when their properties are other well-known types.
- Polymorphic Kotlin sealed classes,
  as described above.
- Kotlin `Unit`
- `UUID` (Java) and `Uuid` (Kotlin)

### Modules

#### [RestModule](../kairo-rest/README.md)

- `HttpMethod`
- `HttpStatusCode`

### Configuration

When creating your `KairoJson` instance,
you can configure Jackson in the constructor.

```kotlin
val json: KairoJson =
  KairoJson {
    allowUnknown = true
    // Alias for configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    pretty = true
    // Alias for configure(SerializationFeature.INDENT_OUTPUT, true)

    configure {
      // Configure Jackson directly.
    }
  }
```

#### Customizing `BigDecimal` serialization

By default, `BigDecimal` serializes to a `Double`.
To use a different approach for serializing `BigDecimal`,
you can either change it globally

```kotlin
val json: KairoJson =
  KairoJson {
    bigDecimalFormat = BigDecimalFormat.String
  }
```

or change it locally

```kotlin
data class MyClass(
  @JsonSerialize(using = BigDecimalSerializer.AsString::class)
  @JsonDeserialize(using = BigDecimalDeserializer.AsString::class)
  val value: BigDecimal,
)
```

#### Customizing `BigInteger` serialization

By default, `BigInteger` serializes to a `Long`.
To use a different approach for serializing `BigInteger`,
you can either change it globally

```kotlin
val json: KairoJson =
  KairoJson {
    bigIntegerFormat = BigIntegerFormat.String
  }
```

or change it locally

```kotlin
data class MyClass(
  @JsonSerialize(using = BigIntegerSerializer.String::class)
  @JsonDeserialize(using = BigIntegerDeserializer.String::class)
  val value: BigInteger,
)
```

### Advanced usage

This README only covers the basics of Jackson.
Refer to Jackson's own documentation for advanced usage.

#### Accessing the underlying `JsonMapper`

You can always access the underlying Jackson `JsonMapper` if you need to,
but you must explicitly opt in using `@OptIn(KairoJson.RawJsonMapper::class)`.

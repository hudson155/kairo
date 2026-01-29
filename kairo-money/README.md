# Money

Modern JVM money handling uses `javax.money`
backed by [Moneta](https://javamoney.github.io/ri.html).

```kotlin
val amount = Money.of("123.45", "USD")
```

## Installation

Install `kairo-money`.
You don't need to install `javax.money` or Moneta separately —
they're included by default.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-money")
}
```

## Usage

Refer to the [JavaMoney specification](https://javamoney.github.io/).

### Serialization

For serialization to work, add `MoneyModule()` to your `Json` instance
and mark `Money` instances as `@Contextual`.

```kotlin
val json: KairoJson =
  KairoJson {
    addModule(MoneyModule())
  }
```

```kotlin
json.serialize(Money.of("123.45", "USD"))
// => {"amount":123.45,"currency":"USD"}

json.deserialize<Money>("""{"amount":123.45,"currency":"USD"}""")
// => Money.of("123.45", "USD")
```

#### Custom serializer

If you want to customize `Money` serialization,
set `moneyFormat` when you instantiate `MoneyModule`.

```kotlin
object CustomMoneyFormat : MoneyFormat() {
  override val serializer: JsonSerializer<Money> =
    object : StdSerializer<Money>(Money::class.java) {
      override fun serialize(
        value: Money,
        gen: JsonGenerator,
        provider: SerializerProvider,
      ) {
        // Your implementation.
      }
    }

  override val deserializer: JsonDeserializer<Money> =
    object : StdDeserializer<Money>(Money::class.java) {
      override fun deserialize(
        p: JsonParser,
        ctxt: DeserializationContext,
      ): Money {
        // Your implementation.
      }
    }
}

val json: KairoJson =
  KairoJson {
    addModule(
      MoneyModule {
        moneyFormat = CustomMoneyFormat
      }
    )
  }
```

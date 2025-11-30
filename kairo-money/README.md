# Money

Modern JVM money handling uses `javax.money`
backed by [Moneta](https://javamoney.github.io/ri.html).

```kotlin
val amount = Money.of("123.45", "USD")
```

## Installation

Install `kairo-money`.\
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

For serialization to work, add `moneyModule()` to your `Json` instance
and mark `Money` instances as `@Contextual`.

```kotlin
val json: Json = json { serializersModule += moneyModule() }
```

```kotlin
json.encodeToString(Money.of("123.45", "USD"))
// => {"amount":"123.45","currency":"USD"}

json.decodeFromString<Money>("""{"amount":"123.45","currency":"USD"}""")
// => Money.of("123.45", "USD")
```

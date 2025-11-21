package kairo.money

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.plus
import org.javamoney.moneta.Money
import org.junit.jupiter.api.Test

internal class MoneySerializationTest {
  private val json: Json = Json { serializersModule += moneyModule() }

  @Test
  fun serialize(): Unit =
    runTest {
      json.encodeToString(Money.of(123, "USD"))
        .shouldBe("""{"amount":"123","currency":"USD"}""")
      json.encodeToString(Money.of(123.45, "USD"))
        .shouldBe("""{"amount":"123.45","currency":"USD"}""")
      json.encodeToString(Money.of(123.456, "USD"))
        .shouldBe("""{"amount":"123.456","currency":"USD"}""")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.decodeFromString<Money>("""{"amount":"123","currency":"USD"}""")
        .shouldBe(Money.of(123, "USD"))
      json.decodeFromString<Money>("""{"amount":"123.45","currency":"USD"}""")
        .shouldBe(Money.of(123.45, "USD"))
      json.decodeFromString<Money>("""{"amount":"123.456","currency":"USD"}""")
        .shouldBe(Money.of(123.456, "USD"))
    }
}

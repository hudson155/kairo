package kairo.money

import io.kotest.matchers.shouldBe
import kairo.serialization.KairoJson
import kotlinx.coroutines.test.runTest
import org.javamoney.moneta.Money
import org.junit.jupiter.api.Test

internal class MoneySerializationTest {
  private val json: KairoJson =
    KairoJson {
      addModule(MoneyModule())
    }

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(Money.of(123, "USD"))
        .shouldBe("""{"amount":123,"currency":"USD"}""")
      json.serialize(Money.of(123.45, "USD"))
        .shouldBe("""{"amount":123.45,"currency":"USD"}""")
      json.serialize(Money.of(123.456, "USD"))
        .shouldBe("""{"amount":123.456,"currency":"USD"}""")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Money>("""{"amount":123,"currency":"USD"}""")
        .shouldBe(Money.of(123, "USD"))
      json.deserialize<Money>("""{"amount":123.45,"currency":"USD"}""")
        .shouldBe(Money.of(123.45, "USD"))
      json.deserialize<Money>("""{"amount":123.456,"currency":"USD"}""")
        .shouldBe(Money.of(123.456, "USD"))
    }
}

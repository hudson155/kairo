package kairo.money

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import javax.money.CurrencyUnit
import javax.money.Monetary
import javax.money.UnknownCurrencyException
import kairo.serialization.KairoJson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class CurrencySerializationTest {
  private val json: KairoJson =
    KairoJson {
      addModule(MoneyModule())
    }

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(Monetary.getCurrency("USD"))
        .shouldBe("\"USD\"")
      json.serialize(Monetary.getCurrency("JPY"))
        .shouldBe("\"JPY\"")
      json.serialize(Monetary.getCurrency("TND"))
        .shouldBe("\"TND\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<CurrencyUnit>("\"USD\"")
        .shouldBe(Monetary.getCurrency("USD"))
      json.deserialize<CurrencyUnit>("\"JPY\"")
        .shouldBe(Monetary.getCurrency("JPY"))
      json.deserialize<CurrencyUnit>("\"TND\"")
        .shouldBe(Monetary.getCurrency("TND"))
    }

  @Test
  fun `deserialize, unknown currency`(): Unit =
    runTest {
      shouldThrowExactly<UnknownCurrencyException> {
        json.deserialize<CurrencyUnit>("\"JEF\"")
      }.message.shouldStartWith(
        "Unknown currency code: JEF",
      )
    }
}

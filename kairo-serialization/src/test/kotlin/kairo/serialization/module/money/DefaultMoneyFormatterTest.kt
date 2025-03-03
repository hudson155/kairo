package kairo.serialization.module.money

import com.fasterxml.jackson.databind.json.JsonMapper
import io.github.projectmapk.jackson.module.kogera.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kotlinx.coroutines.test.runTest
import org.javamoney.moneta.Money
import org.junit.jupiter.api.Test

internal class DefaultMoneyFormatterTest {
  private val mapper: JsonMapper = jsonMapper().build()

  private val positiveUsd: Money = Money.of(12_345.67, "USD")
  private val positiveJpy: Money = Money.of(12_345, "JPY")
  private val positiveTnd: Money = Money.of(12_345.678, "TND")

  private val negativeUsd: Money = Money.of(-12_345.67, "USD")
  private val negativeJpy: Money = Money.of(-12_345, "JPY")
  private val negativeTnd: Money = Money.of(-12_345.678, "TND")

  private val noDecimalsUsd: Money = Money.of(12_345, "USD")
  private val noDecimalsJpy: Money = Money.of(12_345, "JPY")
  private val noDecimalsTnd: Money = Money.of(12_345, "TND")

  private val extraDecimalsUsd: Money = Money.of(12_345.675, "USD")
  private val extraDecimalsJpy: Money = Money.of(12_345.5, "JPY")
  private val extraDecimalsTnd: Money = Money.of(12_345.678_5, "TND")

  @Test
  fun `serialize, positive`(): Unit = runTest {
    mapper.writeValueAsString(positiveUsd).shouldBe("{\"amount\":\"12345.67\",\"currency\":\"USD\"}")
    mapper.writeValueAsString(positiveJpy).shouldBe("{\"amount\":\"12345\",\"currency\":\"JPY\"}")
    mapper.writeValueAsString(positiveTnd).shouldBe("{\"amount\":\"12345.678\",\"currency\":\"TND\"}")
  }

  @Test
  fun `serialize, negative`(): Unit = runTest {
    mapper.writeValueAsString(negativeUsd).shouldBe("{\"amount\":\"-12345.67\",\"currency\":\"USD\"}")
    mapper.writeValueAsString(negativeJpy).shouldBe("{\"amount\":\"-12345\",\"currency\":\"JPY\"}")
    mapper.writeValueAsString(negativeTnd).shouldBe("{\"amount\":\"-12345.678\",\"currency\":\"TND\"}")
  }

  @Test
  fun `serialize, no decimals`(): Unit = runTest {
    mapper.writeValueAsString(noDecimalsUsd).shouldBe("{\"amount\":\"12345.00\",\"currency\":\"USD\"}")
    mapper.writeValueAsString(noDecimalsJpy).shouldBe("{\"amount\":\"12345\",\"currency\":\"JPY\"}")
    mapper.writeValueAsString(noDecimalsTnd).shouldBe("{\"amount\":\"12345.000\",\"currency\":\"TND\"}")
  }

  @Test
  fun `serialize, extra decimals`(): Unit = runTest {
    mapper.writeValueAsString(extraDecimalsUsd).shouldBe("{\"amount\":\"12345.675\",\"currency\":\"USD\"}")
    mapper.writeValueAsString(extraDecimalsJpy).shouldBe("{\"amount\":\"12345.5\",\"currency\":\"JPY\"}")
    mapper.writeValueAsString(extraDecimalsTnd).shouldBe("{\"amount\":\"12345.6785\",\"currency\":\"TND\"}")
  }

  @Test
  fun `deserialize, positive`(): Unit = runTest {
    mapper.readValue<Money>("{\"amount\":\"12345.67\",\"currency\":\"USD\"}").shouldBe(positiveUsd)
    mapper.readValue<Money>("{\"amount\":\"12345\",\"currency\":\"JPY\"}").shouldBe(positiveJpy)
    mapper.readValue<Money>("{\"amount\":\"12345.678\",\"currency\":\"TND\"}").shouldBe(positiveTnd)
  }

  @Test
  fun `deserialize, negative`(): Unit = runTest {
    mapper.readValue<Money>("{\"amount\":\"-12345.67\",\"currency\":\"USD\"}").shouldBe(negativeUsd)
    mapper.readValue<Money>("{\"amount\":\"-12345\",\"currency\":\"JPY\"}").shouldBe(negativeJpy)
    mapper.readValue<Money>("{\"amount\":\"-12345.678\",\"currency\":\"TND\"}").shouldBe(negativeTnd)
  }

  @Test
  fun `deserialize, no decimals`(): Unit = runTest {
    mapper.readValue<Money>("{\"amount\":\"12345.00\",\"currency\":\"USD\"}").shouldBe(noDecimalsUsd)
    mapper.readValue<Money>("{\"amount\":\"12345\",\"currency\":\"JPY\"}").shouldBe(noDecimalsJpy)
    mapper.readValue<Money>("{\"amount\":\"12345.000\",\"currency\":\"TND\"}").shouldBe(noDecimalsTnd)
  }

  @Test
  fun `deserialize, extra decimals`(): Unit = runTest {
    mapper.readValue<Money>("{\"amount\":\"12345.675\",\"currency\":\"USD\"}").shouldBe(extraDecimalsUsd)
    mapper.readValue<Money>("{\"amount\":\"12345.5\",\"currency\":\"JPY\"}").shouldBe(extraDecimalsJpy)
    mapper.readValue<Money>("{\"amount\":\"12345.6785\",\"currency\":\"TND\"}").shouldBe(extraDecimalsTnd)
  }
}

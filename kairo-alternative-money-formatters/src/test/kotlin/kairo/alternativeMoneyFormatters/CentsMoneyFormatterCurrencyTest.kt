package kairo.alternativeMoneyFormatters

import com.fasterxml.jackson.databind.json.JsonMapper
import io.github.projectmapk.jackson.module.kogera.convertValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.module.money.moneyFormatter
import kotlinx.coroutines.test.runTest
import org.javamoney.moneta.Money
import org.junit.jupiter.api.Test

internal class CentsMoneyFormatterCurrencyTest {
  private val mapper: JsonMapper = jsonMapper {
    moneyFormatter = CentsMoneyFormatter("USD")
  }.build()

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
    mapper.convertValue<Long>(positiveUsd).shouldBe(1_234_567)
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<Long>(positiveJpy)
    }
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<Long>(positiveTnd)
    }
  }

  @Test
  fun `serialize, negative`(): Unit = runTest {
    mapper.convertValue<Long>(negativeUsd).shouldBe(-1_234_567)
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<Long>(negativeJpy)
    }
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<Long>(negativeTnd)
    }
  }

  @Test
  fun `serialize, no decimals`(): Unit = runTest {
    mapper.convertValue<Long>(noDecimalsUsd).shouldBe(1_234_500)
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<Long>(noDecimalsJpy)
    }
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<Long>(noDecimalsTnd)
    }
  }

  @Test
  fun `serialize, extra decimals`(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<Long>(extraDecimalsUsd)
    }
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<Long>(extraDecimalsJpy)
    }
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<Long>(extraDecimalsTnd)
    }
  }

  @Test
  fun `deserialize, positive`(): Unit = runTest {
    mapper.convertValue<Money>(1_234_567).shouldBe(positiveUsd)
  }

  @Test
  fun `deserialize, negative`(): Unit = runTest {
    mapper.convertValue<Money>(-1_234_567).shouldBe(negativeUsd)
  }

  @Test
  fun `deserialize, no decimals`(): Unit = runTest {
    mapper.convertValue<Money>(1_234_500).shouldBe(noDecimalsUsd)
  }

  @Test
  fun `deserialize, extra decimals`(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<Money>(1_234_567.5).shouldBe(extraDecimalsUsd)
    }
  }
}

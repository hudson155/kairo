package kairo.alternativeMoneyFormatters

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.module.money.moneyFormatter
import kotlinx.coroutines.test.runTest
import org.javamoney.moneta.Money
import org.junit.jupiter.api.Test

internal class AmountAsStringMoneyFormatterNoCurrencyTest {
  private val mapper: JsonMapper = jsonMapper {
    moneyFormatter = AmountAsStringMoneyFormatter()
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
    mapper.convertValue<String>(positiveUsd).shouldBe("12345.67")
    mapper.convertValue<String>(positiveJpy).shouldBe("12345")
    mapper.convertValue<String>(positiveTnd).shouldBe("12345.678")
  }

  @Test
  fun `serialize, negative`(): Unit = runTest {
    mapper.convertValue<String>(negativeUsd).shouldBe("-12345.67")
    mapper.convertValue<String>(negativeJpy).shouldBe("-12345")
    mapper.convertValue<String>(negativeTnd).shouldBe("-12345.678")
  }

  @Test
  fun `serialize, no decimals`(): Unit = runTest {
    mapper.convertValue<String>(noDecimalsUsd).shouldBe("12345.00")
    mapper.convertValue<String>(noDecimalsJpy).shouldBe("12345")
    mapper.convertValue<String>(noDecimalsTnd).shouldBe("12345.000")
  }

  @Test
  fun `serialize, extra decimals`(): Unit = runTest {
    mapper.convertValue<String>(extraDecimalsUsd).shouldBe("12345.675")
    mapper.convertValue<String>(extraDecimalsJpy).shouldBe("12345.5")
    mapper.convertValue<String>(extraDecimalsTnd).shouldBe("12345.6785")
  }

  @Test
  fun `deserialize, positive`(): Unit = runTest {
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>("12345.67")
    }
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>("12345")
    }
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>("12345.678")
    }
  }

  @Test
  fun `deserialize, negative`(): Unit = runTest {
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>("-12345.67")
    }
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>("-12345")
    }
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>("-12345.678")
    }
  }

  @Test
  fun `deserialize, no decimals`(): Unit = runTest {
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>("12345.00")
    }
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>("12345")
    }
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>("12345.000")
    }
  }

  @Test
  fun `deserialize, extra decimals`(): Unit = runTest {
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>("12345.675")
    }
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>("12345.5")
    }
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>("12345.6785")
    }
  }
}

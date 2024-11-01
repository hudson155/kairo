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

internal class CentsMoneyFormatterNoCurrencyTest {
  private val mapper: JsonMapper = jsonMapper {
    moneyFormatter = CentsMoneyFormatter()
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
    mapper.convertValue<Long>(positiveJpy).shouldBe(12_345)
    mapper.convertValue<Long>(positiveTnd).shouldBe(12_345_678)
  }

  @Test
  fun `serialize, negative`(): Unit = runTest {
    mapper.convertValue<Long>(negativeUsd).shouldBe(-1_234_567)
    mapper.convertValue<Long>(negativeJpy).shouldBe(-12_345)
    mapper.convertValue<Long>(negativeTnd).shouldBe(-12_345_678)
  }

  @Test
  fun `serialize, no decimals`(): Unit = runTest {
    mapper.convertValue<Long>(noDecimalsUsd).shouldBe(1_234_500)
    mapper.convertValue<Long>(noDecimalsJpy).shouldBe(12_345)
    mapper.convertValue<Long>(noDecimalsTnd).shouldBe(12_345_000)
  }

  @Test
  fun `serialize, extra decimals`(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<Long>(extraDecimalsUsd).shouldBe(12_345.675)
    }
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<Long>(extraDecimalsJpy).shouldBe(12_345.5)
    }
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<Long>(extraDecimalsTnd).shouldBe(12_345.678_5)
    }
  }

  @Test
  fun `deserialize, positive`(): Unit = runTest {
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>(1_234_567)
    }
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>(12_345)
    }
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>(12_345_678)
    }
  }

  @Test
  fun `deserialize, negative`(): Unit = runTest {
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>(-1_234_567)
    }
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>(-12_345)
    }
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>(-12_345_678)
    }
  }

  @Test
  fun `deserialize, no decimals`(): Unit = runTest {
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>(1_234_500)
    }
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>(12_345)
    }
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>(12_345_000)
    }
  }

  @Test
  fun `deserialize, extra decimals`(): Unit = runTest {
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>(12_345_675)
    }
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>(123_455)
    }
    shouldThrow<UnsupportedOperationException> {
      mapper.convertValue<Money>(123_456_785)
    }
  }
}

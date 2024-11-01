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

internal class CentsMoneyFormatterTest {
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
  private val extraDecimalsTnd: Money = Money.of(12_345.6785, "TND")

  @Test
  fun `serialize, positive`(): Unit = runTest {
    createMapper("USD").convertValue<Long>(positiveUsd).shouldBe(1_234_567)
    createMapper("JPY").convertValue<Long>(positiveJpy).shouldBe(12_345)
    createMapper("TND").convertValue<Long>(positiveTnd).shouldBe(12_345_678)
  }

  @Test
  fun `serialize, negative`(): Unit = runTest {
    createMapper("USD").convertValue<Long>(negativeUsd).shouldBe(-1_234_567)
    createMapper("JPY").convertValue<Long>(negativeJpy).shouldBe(-12_345)
    createMapper("TND").convertValue<Long>(negativeTnd).shouldBe(-12_345_678)
  }

  @Test
  fun `serialize, no decimals`(): Unit = runTest {
    createMapper("USD").convertValue<Long>(noDecimalsUsd).shouldBe(1_234_500)
    createMapper("JPY").convertValue<Long>(noDecimalsJpy).shouldBe(12_345)
    createMapper("TND").convertValue<Long>(noDecimalsTnd).shouldBe(12_345_000)
  }

  @Test
  fun `serialize, extra decimals`(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      createMapper("USD").convertValue<Long>(extraDecimalsUsd)
    }
    shouldThrow<IllegalArgumentException> {
      createMapper("JPY").convertValue<Long>(extraDecimalsJpy)
    }
    shouldThrow<IllegalArgumentException> {
      createMapper("TND").convertValue<Long>(extraDecimalsTnd)
    }
  }

  @Test
  fun `deserialize, positive`(): Unit = runTest {
    createMapper("USD").convertValue<Money>(1_234_567).shouldBe(positiveUsd)
    createMapper("JPY").convertValue<Money>(12_345).shouldBe(positiveJpy)
    createMapper("TND").convertValue<Money>(12_345_678).shouldBe(positiveTnd)
  }

  @Test
  fun `deserialize, negative`(): Unit = runTest {
    createMapper("USD").convertValue<Money>(-1_234_567).shouldBe(negativeUsd)
    createMapper("JPY").convertValue<Money>(-12_345).shouldBe(negativeJpy)
    createMapper("TND").convertValue<Money>(-12_345_678).shouldBe(negativeTnd)
  }

  @Test
  fun `deserialize, no decimals`(): Unit = runTest {
    createMapper("USD").convertValue<Money>(1_234_500).shouldBe(noDecimalsUsd)
    createMapper("JPY").convertValue<Money>(12_345).shouldBe(noDecimalsJpy)
    createMapper("TND").convertValue<Money>(12_345_000).shouldBe(noDecimalsTnd)
  }

  @Test
  fun `deserialize, extra decimals`(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      createMapper("USD").convertValue<Money>(1_234_567.5)
    }
    shouldThrow<IllegalArgumentException> {
      createMapper("JPY").convertValue<Money>(12_345.5)
    }
    shouldThrow<IllegalArgumentException> {
      createMapper("TND").convertValue<Money>(12_345_678.5)
    }
  }

  @Test
  fun `serialize, wrong currency`(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      createMapper("CAD").convertValue<Long>(positiveUsd).shouldBe(1_234_567)
    }
  }

  private fun createMapper(currencyCode: String): JsonMapper =
    jsonMapper {
      moneyFormatter = CentsMoneyFormatter(currencyCode)
    }.build()
}

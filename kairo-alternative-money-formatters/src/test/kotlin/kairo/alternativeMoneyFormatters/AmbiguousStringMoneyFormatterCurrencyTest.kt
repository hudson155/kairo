package kairo.alternativeMoneyFormatters

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import java.text.ParseException
import kairo.serialization.jsonMapper
import kairo.serialization.module.money.moneyFormatter
import kotlinx.coroutines.test.runTest
import org.javamoney.moneta.Money
import org.junit.jupiter.api.Test

internal class AmbiguousStringMoneyFormatterCurrencyTest {
  private val mapper: JsonMapper = jsonMapper {
    moneyFormatter = AmbiguousStringMoneyFormatter("USD")
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
    mapper.convertValue<String>(positiveUsd).shouldBe("$12,345.67")
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<String>(positiveJpy)
    }
    // shouldThrow<IllegalArgumentException> {
    //   mapper.convertValue<String>(positiveTnd)
    // }
  }

  @Test
  fun `serialize, negative`(): Unit = runTest {
    mapper.convertValue<String>(negativeUsd).shouldBe("-$12,345.67")
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<String>(negativeJpy)
    }
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<String>(negativeTnd)
    }
  }

  @Test
  fun `serialize, no decimals`(): Unit = runTest {
    mapper.convertValue<String>(noDecimalsUsd).shouldBe("$12,345.00")
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<String>(noDecimalsJpy)
    }
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<String>(noDecimalsTnd)
    }
  }

  @Test
  fun `serialize, extra decimals`(): Unit = runTest {
    mapper.convertValue<String>(extraDecimalsUsd).shouldBe("$12,345.675")
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<String>(extraDecimalsJpy)
    }
    shouldThrow<IllegalArgumentException> {
      mapper.convertValue<String>(extraDecimalsTnd)
    }
  }

  @Test
  fun `deserialize, positive`(): Unit = runTest {
    mapper.convertValue<Money>("$12,345.67").shouldBe(positiveUsd)
    shouldThrow<ParseException> {
      mapper.convertValue<Money>("¥12,345")
    }
    shouldThrow<ParseException> {
      mapper.convertValue<Money>("TND12,345.678")
    }
  }

  @Test
  fun `deserialize, negative`(): Unit = runTest {
    mapper.convertValue<Money>("-$12,345.67").shouldBe(negativeUsd)
    shouldThrow<ParseException> {
      mapper.convertValue<Money>("-¥12,345")
    }
    shouldThrow<ParseException> {
      mapper.convertValue<Money>("-TND12,345.678")
    }
  }

  @Test
  fun `deserialize, no decimals`(): Unit = runTest {
    mapper.convertValue<Money>("$12,345.00").shouldBe(noDecimalsUsd)
    shouldThrow<ParseException> {
      mapper.convertValue<Money>("¥12,345")
    }
    shouldThrow<ParseException> {
      mapper.convertValue<Money>("TND12,345.000")
    }
  }

  @Test
  fun `deserialize, extra decimals`(): Unit = runTest {
    mapper.convertValue<Money>("$12,345.675").shouldBe(extraDecimalsUsd)
    shouldThrow<ParseException> {
      mapper.convertValue<Money>("¥12,345.5")
    }
    shouldThrow<ParseException> {
      mapper.convertValue<Money>("TND12,345.6785")
    }
  }
}

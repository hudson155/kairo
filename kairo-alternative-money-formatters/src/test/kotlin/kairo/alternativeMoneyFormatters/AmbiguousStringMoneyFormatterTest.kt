package kairo.alternativeMoneyFormatters

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.module.money.moneyFormatter
import kairo.serialization.property.prettyPrint
import kotlinx.coroutines.test.runTest
import org.javamoney.moneta.Money
import org.junit.jupiter.api.Test

internal class AmbiguousStringMoneyFormatterTest {
  internal data class MyClass(
    val usd: Money,
    val jpy: Money,
    val tnd: Money,
  )

  private val mapper: JsonMapper = jsonMapper {
    prettyPrint = true
    moneyFormatter = AmbiguousStringMoneyFormatter
  }.build()

  private val positive: MyClass =
    MyClass(
      usd = Money.of(12_345.67, "USD"),
      jpy = Money.of(12_345, "JPY"),
      tnd = Money.of(12_345.678, "TND"),
    )

  private val negative: MyClass =
    MyClass(
      usd = Money.of(-12_345.67, "USD"),
      jpy = Money.of(-12_345, "JPY"),
      tnd = Money.of(-12_345.678, "TND"),
    )

  private val noDecimals: MyClass =
    MyClass(
      usd = Money.of(12_345, "USD"),
      jpy = Money.of(12_345, "JPY"),
      tnd = Money.of(12_345, "TND"),
    )

  private val extraDecimals: MyClass =
    MyClass(
      usd = Money.of(12_345.675, "USD"),
      jpy = Money.of(12_345.5, "JPY"),
      tnd = Money.of(12_345.6785, "TND"),
    )

  @Test
  fun `serialize, ambiguous string, positive`(): Unit = runTest {
    mapper.writeValueAsString(positive).shouldBe(
      """
        {
          "jpy": "¥12,345",
          "tnd": "TND12,345.678",
          "usd": "$12,345.67"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `serialize, ambiguous string, negative`(): Unit = runTest {
    mapper.writeValueAsString(negative).shouldBe(
      """
        {
          "jpy": "-¥12,345",
          "tnd": "-TND12,345.678",
          "usd": "-$12,345.67"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `serialize, ambiguous string, no decimals`(): Unit = runTest {
    mapper.writeValueAsString(noDecimals).shouldBe(
      """
        {
          "jpy": "¥12,345",
          "tnd": "TND12,345.000",
          "usd": "$12,345.00"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `serialize, ambiguous string, extra decimals`(): Unit = runTest {
    mapper.writeValueAsString(extraDecimals).shouldBe(
      """
        {
          "jpy": "¥12,345.5",
          "tnd": "TND12,345.6785",
          "usd": "$12,345.675"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `deserialize, ambiguous string, positive`(): Unit = runTest {
    shouldThrow<NotImplementedError> {
      mapper.readValue<MyClass>(
        """
          {
            "jpy": "¥12,345",
            "tnd": "TND12,345.678",
            "usd": "$12,345.67"
          }
        """.trimIndent(),
      )
    }
  }

  @Test
  fun `deserialize, ambiguous string, negative`(): Unit = runTest {
    shouldThrow<NotImplementedError> {
      mapper.readValue<MyClass>(
        """
          {
            "jpy": "-¥12,345",
            "tnd": "-TND12,345.678",
            "usd": "-$12,345.67"
          }
        """.trimIndent(),
      )
    }
  }

  @Test
  fun `deserialize, ambiguous string, no decimals`(): Unit = runTest {
    shouldThrow<NotImplementedError> {
      mapper.readValue<MyClass>(
        """
          {
            "jpy": "¥12,345",
            "tnd": "TND12,345.000",
            "usd": "$12,345.00"
          }
        """.trimIndent(),
      )
    }
  }

  @Test
  fun `deserialize, ambiguous string, extra decimals`(): Unit = runTest {
    shouldThrow<NotImplementedError> {
      mapper.readValue<MyClass>(
        """
          {
            "jpy": "¥12,345.5",
            "tnd": "TND12,345.6785",
            "usd": "$12,345.675"
          }
        """.trimIndent(),
      )
    }
  }
}

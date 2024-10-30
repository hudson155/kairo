package kairo.serialization.module.money

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.property.prettyPrint
import kotlinx.coroutines.test.runTest
import org.javamoney.moneta.Money
import org.junit.jupiter.api.Test

internal class DefaultMoneyFormatterTest {
  internal data class MyClass(
    val usd: Money,
    val jpy: Money,
    val tnd: Money,
  )

  private val mapper: JsonMapper = jsonMapper {
    prettyPrint = true
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
  fun `serialize, default, positive`(): Unit = runTest {
    mapper.writeValueAsString(positive).shouldBe(
      """
        {
          "jpy": {
            "amount": "12345",
            "currency": "JPY"
          },
          "tnd": {
            "amount": "12345.678",
            "currency": "TND"
          },
          "usd": {
            "amount": "12345.67",
            "currency": "USD"
          }
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `serialize, default, negative`(): Unit = runTest {
    mapper.writeValueAsString(negative).shouldBe(
      """
        {
          "jpy": {
            "amount": "-12345",
            "currency": "JPY"
          },
          "tnd": {
            "amount": "-12345.678",
            "currency": "TND"
          },
          "usd": {
            "amount": "-12345.67",
            "currency": "USD"
          }
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `serialize, default, no decimals`(): Unit = runTest {
    mapper.writeValueAsString(noDecimals).shouldBe(
      """
        {
          "jpy": {
            "amount": "12345",
            "currency": "JPY"
          },
          "tnd": {
            "amount": "12345.000",
            "currency": "TND"
          },
          "usd": {
            "amount": "12345.00",
            "currency": "USD"
          }
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `serialize, default, extra decimals`(): Unit = runTest {
    mapper.writeValueAsString(extraDecimals).shouldBe(
      """
        {
          "jpy": {
            "amount": "12345.5",
            "currency": "JPY"
          },
          "tnd": {
            "amount": "12345.6785",
            "currency": "TND"
          },
          "usd": {
            "amount": "12345.675",
            "currency": "USD"
          }
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `deserialize, default, positive`(): Unit = runTest {
    mapper.readValue<MyClass>(
      """
        {
          "jpy": {
            "amount": "12345",
            "currency": "JPY"
          },
          "tnd": {
            "amount": "12345.678",
            "currency": "TND"
          },
          "usd": {
            "amount": "12345.67",
            "currency": "USD"
          }
        }
      """.trimIndent(),
    ).shouldBe(positive)
  }

  @Test
  fun `deserialize, default, negative`(): Unit = runTest {
    mapper.readValue<MyClass>(
      """
        {
          "jpy": {
            "amount": "-12345",
            "currency": "JPY"
          },
          "tnd": {
            "amount": "-12345.678",
            "currency": "TND"
          },
          "usd": {
            "amount": "-12345.67",
            "currency": "USD"
          }
        }
      """.trimIndent(),
    ).shouldBe(negative)
  }

  @Test
  fun `deserialize, default, no decimals`(): Unit = runTest {
    mapper.readValue<MyClass>(
      """
        {
          "jpy": {
            "amount": "12345",
            "currency": "JPY"
          },
          "tnd": {
            "amount": "12345.000",
            "currency": "TND"
          },
          "usd": {
            "amount": "12345.00",
            "currency": "USD"
          }
        }
      """.trimIndent(),
    ).shouldBe(noDecimals)
  }

  @Test
  fun `deserialize, default, extra decimals`(): Unit = runTest {
    mapper.readValue<MyClass>(
      """
        {
          "jpy": {
            "amount": "12345.5",
            "currency": "JPY"
          },
          "tnd": {
            "amount": "12345.6785",
            "currency": "TND"
          },
          "usd": {
            "amount": "12345.675",
            "currency": "USD"
          }
        }
      """.trimIndent(),
    ).shouldBe(extraDecimals)
  }
}

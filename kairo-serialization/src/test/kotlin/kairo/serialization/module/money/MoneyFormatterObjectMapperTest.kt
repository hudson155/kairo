package kairo.serialization.module.money

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.property.prettyPrint
import kotlinx.coroutines.test.runTest
import org.javamoney.moneta.Money
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to money formatting.
 * Therefore, some test cases are not included since they are not strictly related to money formatting.
 */
internal class MoneyFormatterObjectMapperTest {
  internal data class MyClass(
    val usd: Money,
    val jpy: Money,
    val tnd: Money,
  )

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
    val mapper = createMapper()
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
    val mapper = createMapper()
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
    val mapper = createMapper()
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
    val mapper = createMapper()
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
  fun `serialize, ambiguous string, positive`(): Unit = runTest {
    val mapper = createMapper(AmbiguousStringMoneyFormatter)
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
    val mapper = createMapper(AmbiguousStringMoneyFormatter)
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
    val mapper = createMapper(AmbiguousStringMoneyFormatter)
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
    val mapper = createMapper(AmbiguousStringMoneyFormatter)
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
  fun `serialize, amount as string, positive`(): Unit = runTest {
    val mapper = createMapper(AmountAsStringMoneyFormatter)
    mapper.writeValueAsString(positive).shouldBe(
      """
        {
          "jpy": "12345",
          "tnd": "12345.678",
          "usd": "12345.67"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `serialize, amount as string, negative`(): Unit = runTest {
    val mapper = createMapper(AmountAsStringMoneyFormatter)
    mapper.writeValueAsString(negative).shouldBe(
      """
        {
          "jpy": "-12345",
          "tnd": "-12345.678",
          "usd": "-12345.67"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `serialize, amount as string, no decimals`(): Unit = runTest {
    val mapper = createMapper(AmountAsStringMoneyFormatter)
    mapper.writeValueAsString(noDecimals).shouldBe(
      """
        {
          "jpy": "12345",
          "tnd": "12345.000",
          "usd": "12345.00"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `serialize, amount as string, extra decimals`(): Unit = runTest {
    val mapper = createMapper(AmountAsStringMoneyFormatter)
    mapper.writeValueAsString(extraDecimals).shouldBe(
      """
        {
          "jpy": "12345.5",
          "tnd": "12345.6785",
          "usd": "12345.675"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `deserialize, default, positive`(): Unit = runTest {
    val mapper = createMapper()
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
    val mapper = createMapper()
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
    val mapper = createMapper()
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
    val mapper = createMapper()
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

  @Test
  fun `deserialize, ambiguous string, positive`(): Unit = runTest {
    val mapper = createMapper(AmbiguousStringMoneyFormatter)
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
    val mapper = createMapper(AmbiguousStringMoneyFormatter)
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
    val mapper = createMapper(AmbiguousStringMoneyFormatter)
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
    val mapper = createMapper(AmbiguousStringMoneyFormatter)
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

  @Test
  fun `deserialize, amount as string, positive`(): Unit = runTest {
    val mapper = createMapper(AmountAsStringMoneyFormatter)
    shouldThrow<NotImplementedError> {
      mapper.readValue<MyClass>(
        """
          {
            "jpy": "12345",
            "tnd": "12345.678",
            "usd": "12345.67"
          }
        """.trimIndent(),
      )
    }
  }

  @Test
  fun `deserialize, amount as string, negative`(): Unit = runTest {
    val mapper = createMapper(AmountAsStringMoneyFormatter)
    shouldThrow<NotImplementedError> {
      mapper.readValue<MyClass>(
        """
          {
            "jpy": "-12345",
            "tnd": "-12345.678",
            "usd": "-12345.67"
          }
        """.trimIndent(),
      )
    }
  }

  @Test
  fun `deserialize, amount as string, no decimals`(): Unit = runTest {
    val mapper = createMapper(AmountAsStringMoneyFormatter)
    shouldThrow<NotImplementedError> {
      mapper.readValue<MyClass>(
        """
          {
            "jpy": "12345",
            "tnd": "12345.000",
            "usd": "12345.00"
          }
        """.trimIndent(),
      )
    }
  }

  @Test
  fun `deserialize, amount as string, extra decimals`(): Unit = runTest {
    val mapper = createMapper(AmountAsStringMoneyFormatter)
    shouldThrow<NotImplementedError> {
      mapper.readValue<MyClass>(
        """
          {
            "jpy": "12345.5",
            "tnd": "12345.6785",
            "usd": "12345.675"
          }
        """.trimIndent(),
      )
    }
  }

  private fun createMapper(moneyFormatter: MoneyFormatter<*>? = null): JsonMapper =
    jsonMapper {
      prettyPrint = true
      if (moneyFormatter != null) {
        this.moneyFormatter = moneyFormatter
      }
    }.build()
}

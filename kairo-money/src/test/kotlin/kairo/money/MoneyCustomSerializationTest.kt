package kairo.money

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import javax.money.Monetary
import kairo.serialization.KairoJson
import kotlinx.coroutines.test.runTest
import org.javamoney.moneta.Money
import org.junit.jupiter.api.Test

/**
 * Specifically tests when a custom money format is used.
 */
internal class MoneyCustomSerializationTest {
  internal object CustomMoneyFormat : MoneyFormat() {
    override val serializer: JsonSerializer<Money> =
      object : StdSerializer<Money>(Money::class.java) {
        override fun serialize(
          value: Money,
          gen: JsonGenerator,
          provider: SerializerProvider,
        ) {
          val string = listOf(
            value.currency.currencyCode,
            value.number.numberValueExact<BigDecimal>().toPlainString(),
          ).joinToString("|")
          provider.defaultSerializeValue(string, gen)
        }
      }

    override val deserializer: JsonDeserializer<Money> =
      object : StdDeserializer<Money>(Money::class.java) {
        override fun deserialize(
          p: JsonParser,
          ctxt: DeserializationContext,
        ): Money {
          val string = ctxt.readValue(p, String::class.java)
          val components = string.split("|")
          require(components.size == 2)
          return Money.of(BigDecimal(components[1]), Monetary.getCurrency(components[0]))
        }
      }
  }

  private val json: KairoJson =
    KairoJson {
      addModule(
        MoneyModule {
          moneyFormat = CustomMoneyFormat
        },
      )
    }

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(Money.of(123, "USD"))
        .shouldBe("\"USD|123\"")
      json.serialize(Money.of(123.45, "USD"))
        .shouldBe("\"USD|123.45\"")
      json.serialize(Money.of(123.456, "USD"))
        .shouldBe("\"USD|123.456\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Money>("\"USD|123\"")
        .shouldBe(Money.of(123, "USD"))
      json.deserialize<Money>("\"USD|123.45\"")
        .shouldBe(Money.of(123.45, "USD"))
      json.deserialize<Money>("\"USD|123.456\"")
        .shouldBe(Money.of(123.456, "USD"))
    }
}

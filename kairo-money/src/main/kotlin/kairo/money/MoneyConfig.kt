@file:Suppress("MatchingDeclarationName")

package kairo.money

import io.ktor.util.AttributeKey
import java.math.BigDecimal
import javax.money.CurrencyUnit
import kairo.serialization.KairoJson

public enum class MoneyFormat(
  internal val serializer: Lazy<MoneySerializer>,
  internal val deserializer: Lazy<MoneyDeserializer>,
) {
  Object(
    serializer = lazy { MoneySerializer.AsObject() },
    deserializer = lazy { MoneyDeserializer.AsObject() },
  ),
}

internal data class MoneyAsObjectDelegate(
  val amount: BigDecimal,
  val currency: CurrencyUnit,
)

private val key: AttributeKey<MoneyFormat> = AttributeKey("moneyFormat")

public var KairoJson.Builder.moneyFormat: MoneyFormat
  get() = attributes.computeIfAbsent(key) { MoneyFormat.Object }
  set(value) {
    attributes[key] = value
  }

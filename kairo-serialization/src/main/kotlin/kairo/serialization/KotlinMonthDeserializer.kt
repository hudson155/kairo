package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlinx.datetime.Month
import kotlinx.datetime.toKotlinMonth

internal class KotlinMonthDeserializer : StdDeserializer<Month>(
  Month::class.java,
) {
  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): Month {
    val javaMonth = ctxt.readValue(p, java.time.Month::class.java)
    return javaMonth.toKotlinMonth()
  }
}

package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.toKotlinDatePeriod

internal class KotlinDatePeriodDeserializer : StdDeserializer<DatePeriod>(
  DatePeriod::class.java
) {
  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): DatePeriod {
    val javaPeriod = ctxt.readValue(p, java.time.Period::class.java)
    return javaPeriod.toKotlinDatePeriod()
  }
}

package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.toJavaPeriod

internal class KotlinDatePeriodSerializer : StdSerializer<DatePeriod>(
  DatePeriod::class.java
) {
  override fun serialize(
    value: DatePeriod,
    gen: JsonGenerator,
    provider: SerializerProvider
  ) {
    val javaPeriod = value.toJavaPeriod()
    provider.defaultSerializeValue(javaPeriod, gen)
  }
}

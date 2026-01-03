package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlinx.datetime.Month
import kotlinx.datetime.toJavaMonth

internal class KotlinMonthSerializer : StdSerializer<Month>(
  Month::class.java
) {
  override fun serialize(
    value: Month,
    gen: JsonGenerator,
    provider: SerializerProvider
  ) {
    val javaMonth = value.toJavaMonth()
    provider.defaultSerializeValue(javaMonth, gen)
  }
}

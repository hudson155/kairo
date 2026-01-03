package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.toJavaDayOfWeek

internal class KotlinDayOfWeekSerializer : StdSerializer<DayOfWeek>(
  DayOfWeek::class.java
) {
  override fun serialize(
    value: DayOfWeek,
    gen: JsonGenerator,
    provider: SerializerProvider
  ) {
    val javaDayOfWeek = value.toJavaDayOfWeek()
    provider.defaultSerializeValue(javaDayOfWeek, gen)
  }
}

package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlinx.datetime.YearMonth
import kotlinx.datetime.toJavaYearMonth

internal class KotlinYearMonthSerializer : StdSerializer<YearMonth>(
  YearMonth::class.java,
) {
  override fun serialize(
    value: YearMonth,
    gen: JsonGenerator,
    provider: SerializerProvider,
  ) {
    val javaYearMonth = value.toJavaYearMonth()
    provider.defaultSerializeValue(javaYearMonth, gen)
  }
}

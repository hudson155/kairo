package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlin.time.Duration
import kotlin.time.toJavaDuration

internal class KotlinDurationSerializer : StdSerializer<Duration>(
  Duration::class.java,
) {
  override fun serialize(
    value: Duration,
    gen: JsonGenerator,
    provider: SerializerProvider,
  ) {
    val javaDuration = value.toJavaDuration()
    provider.defaultSerializeValue(javaDuration, gen)
  }
}

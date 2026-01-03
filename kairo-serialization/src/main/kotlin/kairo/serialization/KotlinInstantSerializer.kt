package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlin.time.Instant
import kotlin.time.toJavaInstant

internal class KotlinInstantSerializer : StdSerializer<Instant>(
  Instant::class.java,
) {
  override fun serialize(
    value: Instant,
    gen: JsonGenerator,
    provider: SerializerProvider,
  ) {
    val javaInstant = value.toJavaInstant()
    provider.defaultSerializeValue(javaInstant, gen)
  }
}

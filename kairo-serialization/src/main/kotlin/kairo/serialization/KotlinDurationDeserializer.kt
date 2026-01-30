package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

internal class KotlinDurationDeserializer : StdDeserializer<Duration>(
  Duration::class.java,
) {
  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): Duration {
    @Suppress("UnnecessaryFullyQualifiedName")
    val javaDuration = ctxt.readValue(p, java.time.Duration::class.java)
    return javaDuration.toKotlinDuration()
  }
}

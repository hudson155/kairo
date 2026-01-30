package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlin.time.Instant
import kotlin.time.toKotlinInstant

internal class KotlinInstantDeserializer : StdDeserializer<Instant>(
  Instant::class.java,
) {
  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext,
  ): Instant {
    @Suppress("UnnecessaryFullyQualifiedName")
    val javaInstant = ctxt.readValue(p, java.time.Instant::class.java)
    return javaInstant.toKotlinInstant()
  }
}

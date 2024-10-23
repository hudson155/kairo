package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlin.uuid.Uuid

/**
 * Jackson supports [java.util.UUID] by default, but not [kotlin.uuid.Uuid] which we use.
 */
public class UuidSerializer : StdSerializer<Uuid>(Uuid::class.java) {
  public class Key : StdSerializer<Uuid>(Uuid::class.java) {
    override fun serialize(value: Uuid, gen: JsonGenerator, provider: SerializerProvider) {
      val string = convert(value)
      gen.writeFieldName(string)
    }
  }

  override fun serialize(value: Uuid, gen: JsonGenerator, provider: SerializerProvider) {
    val string = convert(value)
    gen.writeString(string)
  }

  public companion object {
    @JvmStatic
    private fun convert(value: Uuid): String =
      value.toString()
  }
}

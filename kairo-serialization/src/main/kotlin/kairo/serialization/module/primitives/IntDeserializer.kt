package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer

public open class IntDeserializer : PrimitiveDeserializer<Int>(Int::class) {
  public open class Key : KeyDeserializer() {
    override fun deserializeKey(key: String, ctxt: DeserializationContext): Int =
      convert(key)
  }

  override val tokens: Set<JsonToken> =
    setOf(JsonToken.VALUE_NUMBER_INT)

  override fun extract(p: JsonParser): Int =
    p.intValue

  public companion object {
    @JvmStatic
    protected fun convert(string: String): Int =
      string.toInt()
  }
}

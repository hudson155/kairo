package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlin.reflect.KClass

/**
 * Kairo uses custom primitive deserializers instead of the built-in Jackson ones.
 */
public abstract class PrimitiveDeserializer<T : Any>(kClass: KClass<T>) : StdDeserializer<T>(kClass.java) {
  public abstract val tokens: Set<JsonToken>

  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T? {
    expectCurrentToken(p, ctxt, tokens)
    return extract(p)
  }

  protected abstract fun extract(p: JsonParser): T
}

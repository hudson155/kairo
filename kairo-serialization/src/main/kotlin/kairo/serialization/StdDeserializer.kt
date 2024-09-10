package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.exc.MismatchedInputException

/**
 * Util function to help with custom serializers and deserializers.
 */
internal fun StdDeserializer<*>.expectCurrentToken(
  p: JsonParser,
  ctxt: DeserializationContext,
  jsonToken: JsonToken,
) {
  expectCurrentToken(p, ctxt, setOf(jsonToken))
}

/**
 * Util function to help with custom serializers and deserializers.
 */
internal fun StdDeserializer<*>.expectCurrentToken(
  p: JsonParser,
  ctxt: DeserializationContext,
  jsonTokens: Set<JsonToken>,
) {
  if (p.currentToken !in jsonTokens) {
    throw MismatchedInputException.from(p, getValueType(ctxt), null)
  }
}

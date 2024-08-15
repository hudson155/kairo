package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.exc.MismatchedInputException

internal fun StdDeserializer<*>.expectCurrentToken(
  p: JsonParser,
  ctxt: DeserializationContext,
  jsonToken: JsonToken,
) {
  if (p.currentToken != jsonToken) {
    throw MismatchedInputException.from(p, getValueType(ctxt), null)
  }
}

package kairo.serialization

import com.fasterxml.jackson.databind.deser.std.StringDeserializer as DelegateStringDeserializer
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.MapperFeature

/**
 * By default, Jackson casts other types (such as booleans and numbers) to strings.
 * Although type coersion can be disabled for scalars using [MapperFeature.ALLOW_COERCION_OF_SCALARS],
 * Jackson provides no option to disable this for strings.
 * Instead, this custom string deserializer checks the type before continuing.
 */
internal class StringDeserializer : DelegateStringDeserializer() {
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String {
    expectCurrentToken(p, ctxt, JsonToken.VALUE_STRING)
    return super.deserialize(p, ctxt)
  }
}

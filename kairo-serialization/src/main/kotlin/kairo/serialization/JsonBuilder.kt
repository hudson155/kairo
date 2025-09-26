package kairo.serialization

import kairo.optional.optionalModule
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.modules.plus

public fun JsonBuilder.kairo() {
  encodeDefaults = true
  explicitNulls = true
  ignoreUnknownKeys = false
  isLenient = false
  coerceInputValues = false
  classDiscriminator = "type"
  useAlternativeNames = false
  decodeEnumsCaseInsensitive = false
  allowTrailingComma = false
  allowComments = false
  allowSpecialFloatingPointValues = false
  serializersModule += optionalModule
}

public fun JsonBuilder.kairoPrettyPrint() {
  prettyPrint = true
  prettyPrintIndent = "  "
}

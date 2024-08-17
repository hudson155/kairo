package kairo.serialization

import com.fasterxml.jackson.databind.JsonMappingException
import io.kotest.assertions.throwables.shouldThrow

/**
 * For simplicity, we only check for [JsonMappingException] to ensure deserialization failed.
 * It's possible but unnecessary to check for more specific exception types.
 */
internal inline fun deserializationShouldFail(block: () -> Any?): JsonMappingException =
  shouldThrow<JsonMappingException>(block)

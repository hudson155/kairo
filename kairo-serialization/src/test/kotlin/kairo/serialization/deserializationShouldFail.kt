package kairo.serialization

import com.fasterxml.jackson.databind.JsonMappingException
import io.kotest.assertions.throwables.shouldThrow

internal inline fun deserializationShouldFail(block: () -> Any?): JsonMappingException =
  shouldThrow<JsonMappingException>(block)

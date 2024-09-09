package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.matchers.types.shouldBeInstanceOf

internal fun shouldBeInsecure(message: String, block: () -> Any?): JsonMappingException =
  shouldThrow<JsonMappingException>(block).apply {
    cause.shouldNotBeNull().apply {
      shouldBeInstanceOf<IllegalArgumentException>()
      shouldHaveMessage(message)
    }
  }

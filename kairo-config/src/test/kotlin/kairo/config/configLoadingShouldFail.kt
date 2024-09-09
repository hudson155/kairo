package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.types.shouldBeInstanceOf

internal fun configLoadingShouldFail(block: () -> Any?): IllegalArgumentException =
  shouldThrow<IllegalArgumentException>(block).apply {
    cause.shouldNotBeNull().apply {
      shouldBeInstanceOf<JsonMappingException>()
    }
  }

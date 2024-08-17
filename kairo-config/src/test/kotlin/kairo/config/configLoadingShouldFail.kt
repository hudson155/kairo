package kairo.config

import com.fasterxml.jackson.databind.JsonMappingException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveCauseInstanceOf

internal fun configLoadingShouldFail(block: () -> Any?): IllegalArgumentException =
  shouldThrow<IllegalArgumentException>(block).apply {
    shouldHaveCauseInstanceOf<JsonMappingException>()
  }

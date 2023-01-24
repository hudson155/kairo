package limber.type

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import limber.serialization.ObjectMapperFactory
import org.junit.jupiter.api.Test

/**
 * These tests are somewhat redundant with [OptionalTest],
 * but redundancy is acceptable here for the sake of clarity.
 */
internal class OptionalOrTest {
  private val objectMapper: ObjectMapper = ObjectMapperFactory.builder(ObjectMapperFactory.Format.Json).build()

  @Test
  fun unspecified() {
    objectMapper.readValue<OptionalWrapper.Nullable>("{}").optional.or("existing")
      .shouldBe("existing")
  }

  @Test
  fun `specified as null`() {
    objectMapper.readValue<OptionalWrapper.Nullable>("{\"optional\":null}").optional.or("existing")
      .shouldBeNull()
  }

  @Test
  fun `specified as not null`() {
    objectMapper.readValue<OptionalWrapper.Nullable>("{\"optional\":\"42\"}").optional.or("existing")
      .shouldBe("42")
  }
}

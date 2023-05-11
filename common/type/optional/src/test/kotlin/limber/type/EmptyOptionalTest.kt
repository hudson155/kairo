package limber.type

import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import java.util.Optional

internal class EmptyOptionalTest : OptionalTest() {
  @Test
  override fun `equals method`() {
    OptionalWrapper.Nullable(Optional.empty()).shouldBe(OptionalWrapper.Nullable(Optional.empty()))
    OptionalWrapper.Nullable(Optional.empty()).shouldNotBe(OptionalWrapper.Nullable(null))
    OptionalWrapper.Nullable(Optional.empty()).shouldNotBe(OptionalWrapper.Nullable(Optional.of("")))
    OptionalWrapper.Nullable(Optional.empty()).shouldNotBe(OptionalWrapper.Nullable(Optional.of("42")))

    OptionalWrapper.NonNullable(Optional.empty()).shouldBe(OptionalWrapper.NonNullable(Optional.empty()))
    OptionalWrapper.NonNullable(Optional.empty()).shouldNotBe(OptionalWrapper.NonNullable(Optional.of("")))
    OptionalWrapper.NonNullable(Optional.empty()).shouldNotBe(OptionalWrapper.NonNullable(Optional.of("42")))
  }

  @Test
  override fun `toString method`() {
    OptionalWrapper.Nullable(Optional.empty()).toString()
      .shouldBe("Nullable(optional=Optional.empty)")

    OptionalWrapper.NonNullable(Optional.empty()).toString()
      .shouldBe("NonNullable(optional=Optional.empty)")
  }

  @Test
  override fun serialize() {
    objectMapper.readValue<OptionalWrapper.Nullable>("{\"optional\":null}")
      .shouldBe(OptionalWrapper.Nullable(Optional.empty()))

    objectMapper.readValue<OptionalWrapper.NonNullable>("{\"optional\":null}")
      .shouldBe(OptionalWrapper.NonNullable(Optional.empty()))
  }

  @Test
  override fun deserialize() {
    objectMapper.writeValueAsString(OptionalWrapper.Nullable(Optional.empty()))
      .shouldBe("{\"optional\":null}")
    objectMapper.writeValueAsString(OptionalWrapper.NonNullable(Optional.empty()))
      .shouldBe("{\"optional\":null}")
  }
}

package limber.type

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import java.util.Optional

internal class NullOptionalTest : OptionalTest() {

  @Test
  override fun `equals method`() {
    OptionalWrapper.Nullable(null).shouldBe(OptionalWrapper.Nullable(null))
    OptionalWrapper.Nullable(null).shouldNotBe(OptionalWrapper.Nullable(Optional.empty()))
    OptionalWrapper.Nullable(null).shouldNotBe(OptionalWrapper.Nullable(Optional.of("")))
    OptionalWrapper.Nullable(null).shouldNotBe(OptionalWrapper.Nullable(Optional.of("42")))
  }

  @Test
  override fun `toString method`() {
    OptionalWrapper.Nullable(null).toString()
      .shouldBe("Nullable(optional=null)")
  }

  @Test
  override fun serialize() {
    objectMapper.writeValueAsString(OptionalWrapper.Nullable(null))
      .shouldBe("{}")
  }

  @Test
  override fun deserialize() {
    objectMapper.readValue<OptionalWrapper.Nullable>("{}")
      .shouldBe(OptionalWrapper.Nullable(null))
    shouldThrow<MissingKotlinParameterException> {
      objectMapper.readValue<OptionalWrapper.NonNullable>("{}")
    }
  }
}

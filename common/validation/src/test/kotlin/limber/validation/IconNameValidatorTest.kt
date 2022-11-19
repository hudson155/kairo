package limber.validation

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import jakarta.validation.Validator
import org.junit.jupiter.api.Test
import java.time.Clock

internal class IconNameValidatorTest {
  private data class WrapperClass(
    @IconNameValidator val value: String,
  )

  private val validator: Validator = ValidatorProvider(Clock.systemUTC()).get()

  @Test
  fun valid() {
    validator.validate(WrapperClass("4k")).shouldBeEmpty()
    validator.validate(WrapperClass("keyboard_double_arrow_down")).shouldBeEmpty()
  }

  @Test
  fun invalid() {
    validator.validate(WrapperClass("")).shouldNotBeEmpty()
    validator.validate(WrapperClass("4")).shouldNotBeEmpty()
    validator.validate(WrapperClass("k")).shouldNotBeEmpty()
    validator.validate(WrapperClass("4K")).shouldNotBeEmpty()
    validator.validate(WrapperClass("Keyboard_double_arrow_down")).shouldNotBeEmpty()
    validator.validate(WrapperClass("keyboard double arrow down")).shouldNotBeEmpty()
  }
}

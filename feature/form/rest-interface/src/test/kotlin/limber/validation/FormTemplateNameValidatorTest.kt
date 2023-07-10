package limber.validation

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import jakarta.validation.Validator
import org.junit.jupiter.api.Test
import java.time.Clock

internal class FormTemplateNameValidatorTest {
  private data class WrapperClass(
    @FormTemplateNameValidator val value: String,
  )

  private val validator: Validator = ValidatorProvider(Clock.systemUTC()).get()

  @Test
  fun valid() {
    validator.validate(WrapperClass("For")).shouldBeEmpty()
    validator.validate(WrapperClass("My form!!! :)")).shouldBeEmpty()
    validator.validate(WrapperClass("A".repeat(63))).shouldBeEmpty()
  }

  @Test
  fun invalid() {
    validator.validate(WrapperClass("")).shouldNotBeEmpty()
    validator.validate(WrapperClass("Fo")).shouldNotBeEmpty()
    validator.validate(WrapperClass("My form £")).shouldNotBeEmpty()
    validator.validate(WrapperClass("A".repeat(64))).shouldNotBeEmpty()
  }
}

package limber.validation

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import jakarta.validation.Validator
import org.junit.jupiter.api.Test
import java.time.Clock

internal class OrganizationNameValidatorTest {
  private data class WrapperClass(
    @OrganizationNameValidator val value: String,
  )

  private val validator: Validator = ValidatorProvider(Clock.systemUTC()).get()

  @Test
  fun valid() {
    validator.validate(WrapperClass("Acm")).shouldBeEmpty()
    validator.validate(WrapperClass("Universal Exports!!! :)")).shouldBeEmpty()
    validator.validate(WrapperClass("A".repeat(31))).shouldBeEmpty()
  }

  @Test
  fun invalid() {
    validator.validate(WrapperClass("")).shouldNotBeEmpty()
    validator.validate(WrapperClass("Ac")).shouldNotBeEmpty()
    validator.validate(WrapperClass("Universal Exports £")).shouldNotBeEmpty()
    validator.validate(WrapperClass("A".repeat(32))).shouldNotBeEmpty()
  }
}

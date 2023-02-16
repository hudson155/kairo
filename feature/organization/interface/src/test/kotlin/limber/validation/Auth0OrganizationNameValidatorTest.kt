package limber.validation

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import jakarta.validation.Validator
import org.junit.jupiter.api.Test
import java.time.Clock

internal class Auth0OrganizationNameValidatorTest {
  private data class WrapperClass(
    @Auth0OrganizationNameValidator val value: String,
  )

  private val validator: Validator = ValidatorProvider(Clock.systemUTC()).get()

  @Test
  fun valid() {
    validator.validate(WrapperClass("abc")).shouldBeEmpty()
    validator.validate(WrapperClass("ABC")).shouldBeEmpty()
    validator.validate(WrapperClass("123")).shouldBeEmpty()
    validator.validate(WrapperClass("a-c")).shouldBeEmpty()
    validator.validate(WrapperClass("1-3")).shouldBeEmpty()
    validator.validate(WrapperClass("a-c-e")).shouldBeEmpty()
    validator.validate(WrapperClass("1-3-5")).shouldBeEmpty()
    validator.validate(WrapperClass("a-${"0".repeat(46)}-z")).shouldBeEmpty()
  }

  @Test
  fun invalid() {
    validator.validate(WrapperClass("")).shouldNotBeEmpty()
    validator.validate(WrapperClass("ab")).shouldNotBeEmpty()
    validator.validate(WrapperClass("12")).shouldNotBeEmpty()
    validator.validate(WrapperClass("-bc")).shouldNotBeEmpty()
    validator.validate(WrapperClass("ab-")).shouldNotBeEmpty()
    validator.validate(WrapperClass("-23")).shouldNotBeEmpty()
    validator.validate(WrapperClass("12-")).shouldNotBeEmpty()
    validator.validate(WrapperClass("a--d")).shouldNotBeEmpty()
    validator.validate(WrapperClass("1--4")).shouldNotBeEmpty()
    validator.validate(WrapperClass("a-${"0".repeat(47)}-z")).shouldNotBeEmpty()
  }
}

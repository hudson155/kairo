package limber.validation

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import jakarta.validation.Validator
import org.junit.jupiter.api.Test
import java.time.Clock

internal class Auth0OrganizationIdValidatorTest {
  private data class WrapperClass(
    @Auth0OrganizationIdValidator val value: String,
  )

  private val validator: Validator = ValidatorProvider(Clock.systemUTC()).get()

  @Test
  fun valid() {
    validator.validate(WrapperClass("org_yDiVK18hoeddya8J")).shouldBeEmpty()
  }

  @Test
  fun invalid() {
    validator.validate(WrapperClass("")).shouldNotBeEmpty()
    validator.validate(WrapperClass("org_yDiVK18hoeddya8")).shouldNotBeEmpty()
    validator.validate(WrapperClass("org_yDiVK18hoeddya-8")).shouldNotBeEmpty()
    validator.validate(WrapperClass("org_yDiVK18hoeddya8JK")).shouldNotBeEmpty()
  }
}

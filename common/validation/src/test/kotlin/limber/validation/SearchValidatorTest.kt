package limber.validation

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import jakarta.validation.Validator
import org.junit.jupiter.api.Test
import java.time.Clock

internal class SearchValidatorTest {
  private data class WrapperClass(
    @SearchValidator val value: String,
  )

  private val validator: Validator = ValidatorProvider(Clock.systemUTC()).get()

  @Test
  fun valid() {
    validator.validate(WrapperClass("Hom")).shouldBeEmpty()
    validator.validate(WrapperClass("Forms!!! :)")).shouldBeEmpty()
    validator.validate(WrapperClass("A".repeat(31))).shouldBeEmpty()
  }

  @Test
  fun invalid() {
    validator.validate(WrapperClass("")).shouldNotBeEmpty()
    validator.validate(WrapperClass("Ho")).shouldNotBeEmpty()
    validator.validate(WrapperClass("Forms £")).shouldNotBeEmpty()
    validator.validate(WrapperClass("A".repeat(32))).shouldNotBeEmpty()
  }
}

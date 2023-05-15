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
    validator.validate(WrapperClass("Que")).shouldBeEmpty()
    validator.validate(WrapperClass("Search!!! :)")).shouldBeEmpty()
    validator.validate(WrapperClass("A".repeat(31))).shouldBeEmpty()
  }

  @Test
  fun invalid() {
    validator.validate(WrapperClass("")).shouldNotBeEmpty()
    validator.validate(WrapperClass("Qu")).shouldNotBeEmpty()
    validator.validate(WrapperClass("Search £")).shouldNotBeEmpty()
    validator.validate(WrapperClass("A".repeat(32))).shouldNotBeEmpty()
  }
}

package limber.validation

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import jakarta.validation.Validator
import org.junit.jupiter.api.Test
import java.time.Clock

internal class HostnameValidatorTest {
  private data class WrapperClass(
    @HostnameValidator val value: String,
  )

  private val validator: Validator = ValidatorProvider(Clock.systemUTC()).get()

  @Test
  fun valid() {
    validator.validate(WrapperClass("localhost")).shouldBeEmpty()
    validator.validate(WrapperClass("jhudson.ca")).shouldBeEmpty()
    validator.validate(WrapperClass("foo.bar.baz")).shouldBeEmpty()
  }

  @Test
  fun invalid() {
    validator.validate(WrapperClass("")).shouldNotBeEmpty()
    validator.validate(WrapperClass("foo.bar.baz/homepage")).shouldNotBeEmpty()
    validator.validate(WrapperClass("http://foo.bar.baz")).shouldNotBeEmpty()
  }
}

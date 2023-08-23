package limber.validation

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import jakarta.validation.Validator
import org.junit.jupiter.api.Test
import java.time.Clock

internal class FeaturePathValidatorTest {
  private data class WrapperClass(
    @FeaturePathValidator val value: String,
  )

  private val validator: Validator = ValidatorProvider(Clock.systemUTC()).get()

  @Test
  fun valid() {
    validator.validate(WrapperClass("/foo")).shouldBeEmpty()
    validator.validate(WrapperClass("/FOO")).shouldBeEmpty()
    validator.validate(WrapperClass("/foo-bar")).shouldBeEmpty()
    validator.validate(WrapperClass("/foo-123")).shouldBeEmpty()
    validator.validate(WrapperClass("/${"a".repeat(31)}")).shouldBeEmpty()
  }

  @Test
  fun invalid() {
    validator.validate(WrapperClass("")).shouldNotBeEmpty()
    validator.validate(WrapperClass("/fo")).shouldNotBeEmpty()
    validator.validate(WrapperClass("foo")).shouldNotBeEmpty()
    validator.validate(WrapperClass("/")).shouldNotBeEmpty()
    validator.validate(WrapperClass("/-foo")).shouldNotBeEmpty()
    validator.validate(WrapperClass("/foo-")).shouldNotBeEmpty()
    validator.validate(WrapperClass("/foo--bar")).shouldNotBeEmpty()
    validator.validate(WrapperClass("/foo/")).shouldNotBeEmpty()
    validator.validate(WrapperClass("/foo/bar")).shouldNotBeEmpty()
    validator.validate(WrapperClass("/foo/bar/")).shouldNotBeEmpty()
    validator.validate(WrapperClass("/${"a".repeat(32)}")).shouldNotBeEmpty()
  }
}
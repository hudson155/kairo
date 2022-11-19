package limber.validation

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import org.junit.jupiter.api.Test
import java.time.Clock

internal class TrueValidatorTest {
  private data class WrapperClass(
    @TrueValidator val value: Boolean,
  )

  @Test
  fun valid() {
    val result = ValidatorProvider(Clock.systemUTC()).get().validate(WrapperClass(true))
    result.shouldBeEmpty()
  }

  @Test
  fun invalid() {
    val result = ValidatorProvider(Clock.systemUTC()).get().validate(WrapperClass(false))
    result.shouldNotBeEmpty()
  }
}

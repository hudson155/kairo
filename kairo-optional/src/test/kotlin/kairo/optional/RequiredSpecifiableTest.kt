package kairo.optional

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class RequiredSpecifiableTest {
  @Test
  fun `isSpecified, missing`(): Unit =
    runTest {
      Required.Missing.isSpecified.shouldBeFalse()
    }

  @Test
  fun `isSpecified, value`(): Unit =
    runTest {
      Required.Value(42).isSpecified.shouldBeTrue()
    }

  @Test
  fun `ifSpecified, missing`(): Unit =
    runTest {
      var specified = false
      Required.Missing.ifSpecified { specified = true }
      specified.shouldBeFalse()
    }

  @Test
  fun `ifSpecified, value`(): Unit =
    runTest {
      var specified = false
      Required.Value(42).ifSpecified { specified = true }
      specified.shouldBeTrue()
    }
}

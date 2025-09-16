package kairo.optional

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class OptionalSpecifiableTest {
  @Test
  fun `isSpecified, missing`(): Unit =
    runTest {
      Optional.Missing.isSpecified.shouldBeFalse()
    }

  @Test
  fun `isSpecified, null`(): Unit =
    runTest {
      Optional.Null.isSpecified.shouldBeTrue()
    }

  @Test
  fun `isSpecified, value`(): Unit =
    runTest {
      Optional.Value(42).isSpecified.shouldBeTrue()
    }

  @Test
  fun `ifSpecified, missing`(): Unit =
    runTest {
      var specified = false
      Optional.Missing.ifSpecified { specified = true }
      specified.shouldBeFalse()
    }

  @Test
  fun `ifSpecified, null`(): Unit =
    runTest {
      var specified = false
      Optional.Null.ifSpecified { specified = true }
      specified.shouldBeTrue()
    }

  @Test
  fun `ifSpecified, value`(): Unit =
    runTest {
      var specified = false
      Optional.Value(42).ifSpecified { specified = true }
      specified.shouldBeTrue()
    }
}

@file:Suppress("InvalidPackageDeclaration")

package kotlin.collections

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class IterableSingleNullOrThrowTest {
  @Test
  fun `iteration should not progress past 2nd element`(): Unit =
    runTest {
      var i = 0
      shouldThrow<IllegalArgumentException> {
        sequence {
          while (true) yield(++i)
        }.asIterable().singleNullOrThrow()
      }
      i.shouldBe(2)
    }
}

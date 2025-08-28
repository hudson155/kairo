@file:Suppress("InvalidPackageDeclaration")

package kotlin.collections

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class SetSingleNullOrThrowTest {
  @Test
  fun `no predicate, 0 elements`(): Unit =
    runTest {
      emptySet<Int>()
        .singleNullOrThrow()
        .shouldBeNull()
    }

  @Test
  fun `no predicate, 1 element`(): Unit =
    runTest {
      setOf(9)
        .singleNullOrThrow()
        .shouldBe(9)
    }

  @Test
  fun `no predicate, 2 elements`(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        setOf(4, 8)
          .singleNullOrThrow()
      }
    }

  @Test
  fun `no predicate, nulls`(): Unit =
    runTest {
      setOf<Int?>(null)
        .singleNullOrThrow()
        .shouldBeNull()
      shouldThrow<IllegalArgumentException> {
        setOf(4, null)
          .singleNullOrThrow()
      }
      shouldThrow<IllegalArgumentException> {
        setOf(null, 4)
          .singleNullOrThrow()
      }
    }

  @Test
  fun `with predicate, 0 matches`(): Unit =
    runTest {
      emptySet<Int>()
        .singleNullOrThrow { true }
        .shouldBeNull()
      setOf(3, 4, 5)
        .singleNullOrThrow { it > 5 }
        .shouldBeNull()
    }

  @Test
  fun `with predicate, 1 match`(): Unit =
    runTest {
      setOf(9)
        .singleNullOrThrow { true }
        .shouldBe(9)
      setOf(3, 4, 5)
        .singleNullOrThrow { it > 4 }
        .shouldBe(5)
    }

  @Test
  fun `with predicate, 2 matches`(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        setOf(4, 8)
          .singleNullOrThrow { true }
      }
      shouldThrow<IllegalArgumentException> {
        setOf(3, 4, 5)
          .singleNullOrThrow { it > 3 }
      }
    }
}

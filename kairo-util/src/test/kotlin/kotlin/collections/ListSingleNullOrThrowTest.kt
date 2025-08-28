@file:Suppress("InvalidPackageDeclaration")

package kotlin.collections

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ListSingleNullOrThrowTest {
  @Test
  fun `no predicate, 0 elements`(): Unit =
    runTest {
      emptyList<Int>()
        .singleNullOrThrow()
        .shouldBeNull()
    }

  @Test
  fun `no predicate, 1 element`(): Unit =
    runTest {
      listOf(9)
        .singleNullOrThrow()
        .shouldBe(9)
    }

  @Test
  fun `no predicate, 2 elements`(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        listOf(4, 8)
          .singleNullOrThrow()
      }
    }

  @Test
  fun `no predicate, nulls`(): Unit =
    runTest {
      listOf<Int?>(null)
        .singleNullOrThrow()
        .shouldBeNull()
      shouldThrow<IllegalArgumentException> {
        listOf(4, null)
          .singleNullOrThrow()
      }
      shouldThrow<IllegalArgumentException> {
        listOf(null, 4)
          .singleNullOrThrow()
      }
    }

  @Test
  fun `with predicate, 0 matches`(): Unit =
    runTest {
      emptyList<Int>()
        .singleNullOrThrow { true }
        .shouldBeNull()
      listOf(3, 4, 5)
        .singleNullOrThrow { it > 5 }
        .shouldBeNull()
    }

  @Test
  fun `with predicate, 1 match`(): Unit =
    runTest {
      listOf(9)
        .singleNullOrThrow { true }
        .shouldBe(9)
      listOf(3, 4, 5)
        .singleNullOrThrow { it > 4 }
        .shouldBe(5)
    }

  @Test
  fun `with predicate, 2 matches`(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        listOf(4, 8)
          .singleNullOrThrow { true }
      }
      shouldThrow<IllegalArgumentException> {
        listOf(3, 4, 5)
          .singleNullOrThrow { it > 3 }
      }
    }
}

@file:Suppress("InvalidPackageDeclaration")

package kotlin.collections

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class SequenceSingleNullOrThrowTest {
  @Test
  fun `no predicate, 0 elements`(): Unit =
    runTest {
      emptySequence<Int>()
        .singleNullOrThrow()
        .shouldBeNull()
    }

  @Test
  fun `no predicate, 1 element`(): Unit =
    runTest {
      sequenceOf(9)
        .singleNullOrThrow()
        .shouldBe(9)
    }

  @Test
  fun `no predicate, 2 elements`(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        sequenceOf(4, 8)
          .singleNullOrThrow()
      }
    }

  @Test
  fun `no predicate, nulls`(): Unit =
    runTest {
      sequenceOf<Int?>(null)
        .singleNullOrThrow()
        .shouldBeNull()
      shouldThrow<IllegalArgumentException> {
        sequenceOf(4, null)
          .singleNullOrThrow()
      }
      shouldThrow<IllegalArgumentException> {
        sequenceOf(null, 4)
          .singleNullOrThrow()
      }
    }

  @Test
  fun `with predicate, 0 matches`(): Unit =
    runTest {
      emptySequence<Int>()
        .singleNullOrThrow { true }
        .shouldBeNull()
      sequenceOf(3, 4, 5)
        .singleNullOrThrow { it > 5 }
        .shouldBeNull()
    }

  @Test
  fun `with predicate, 1 match`(): Unit =
    runTest {
      sequenceOf(9)
        .singleNullOrThrow { true }
        .shouldBe(9)
      sequenceOf(3, 4, 5)
        .singleNullOrThrow { it > 4 }
        .shouldBe(5)
    }

  @Test
  fun `with predicate, 2 matches`(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        sequenceOf(4, 8)
          .singleNullOrThrow { true }
      }
      shouldThrow<IllegalArgumentException> {
        sequenceOf(3, 4, 5)
          .singleNullOrThrow { it > 3 }
      }
    }

  @Test
  fun `iteration should not progress past 2nd element`(): Unit =
    runTest {
      var i = 0
      shouldThrow<IllegalArgumentException> {
        sequence {
          while (true) yield(++i)
        }.singleNullOrThrow()
      }
      i.shouldBe(2)
    }
}

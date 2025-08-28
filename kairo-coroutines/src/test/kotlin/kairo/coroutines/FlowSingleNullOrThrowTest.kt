package kairo.coroutines

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class FlowSingleNullOrThrowTest {
  @Test
  fun `no predicate, 0 elements`(): Unit =
    runTest {
      emptyFlow<Int>()
        .singleNullOrThrow()
        .shouldBeNull()
    }

  @Test
  fun `no predicate, 1 element`(): Unit =
    runTest {
      flowOf(9)
        .singleNullOrThrow()
        .shouldBe(9)
    }

  @Test
  fun `no predicate, 2 elements`(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        flowOf(4, 8)
          .singleNullOrThrow()
      }
    }

  @Test
  fun `no predicate, nulls`(): Unit =
    runTest {
      flowOf<Int?>(null)
        .singleNullOrThrow()
        .shouldBeNull()
      shouldThrow<IllegalArgumentException> {
        flowOf(4, null)
          .singleNullOrThrow()
      }
      shouldThrow<IllegalArgumentException> {
        flowOf(null, 4)
          .singleNullOrThrow()
      }
    }

  @Test
  fun `with predicate, 0 matches`(): Unit =
    runTest {
      emptyFlow<Int>()
        .singleNullOrThrow { true }
        .shouldBeNull()
      flowOf(3, 4, 5)
        .singleNullOrThrow { it > 5 }
        .shouldBeNull()
    }

  @Test
  fun `with predicate, 1 match`(): Unit =
    runTest {
      flowOf(9)
        .singleNullOrThrow { true }
        .shouldBe(9)
      flowOf(3, 4, 5)
        .singleNullOrThrow { it > 4 }
        .shouldBe(5)
    }

  @Test
  fun `with predicate, 2 matches`(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        flowOf(4, 8)
          .singleNullOrThrow { true }
      }
      shouldThrow<IllegalArgumentException> {
        flowOf(3, 4, 5)
          .singleNullOrThrow { it > 3 }
      }
    }

  @Test
  fun `iteration should not progress past 2nd element`(): Unit =
    runTest {
      var i = 0
      shouldThrow<IllegalArgumentException> {
        flow {
          while (true) emit(++i)
        }.singleNullOrThrow()
      }
      i.shouldBe(2)
    }
}

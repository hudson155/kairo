@file:Suppress("InvalidPackageDeclaration")

package kotlin.collections

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.util.ArrayDeque
import java.util.Queue
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("ArrayPrimitive")
internal class SingleNullOrThrowTest {
  @Test
  fun `no predicate, 0 elements, array`() =
    runTest {
      val array: Array<Int> = emptyArray<Int>()
      array.singleNullOrThrow()
        .shouldBeNull()
    }

  @Test
  fun `no predicate, 0 elements, list`() =
    runTest {
      val list: List<Int> = emptyList()
      list.singleNullOrThrow()
        .shouldBeNull()
    }

  @Test
  fun `no predicate, 0 elements, queue`() =
    runTest {
      val queue: Queue<Int> = ArrayDeque(emptyList())
      queue.singleNullOrThrow()
        .shouldBeNull()
    }

  @Test
  fun `no predicate, 1 element, array`() =
    runTest {
      val array: Array<Int> = arrayOf(9)
      array.singleNullOrThrow()
        .shouldBe(9)
    }

  @Test
  fun `no predicate, 1 element, list`() =
    runTest {
      val list: List<Int> = listOf(9)
      list.singleNullOrThrow()
        .shouldBe(9)
    }

  @Test
  fun `no predicate, 1 element, queue`() =
    runTest {
      val queue: Queue<Int> = ArrayDeque(listOf(9))
      queue.singleNullOrThrow()
        .shouldBe(9)
    }

  @Test
  fun `no predicate, 2 elements, array`() =
    runTest {
      val array: Array<Int> = arrayOf(4, 8)
      shouldThrow<IllegalArgumentException> {
        array.singleNullOrThrow()
      }
    }

  @Test
  fun `no predicate, 2 elements, list`() =
    runTest {
      val list: List<Int> = listOf(4, 8)
      shouldThrow<IllegalArgumentException> {
        list.singleNullOrThrow()
      }
    }

  @Test
  fun `no predicate, 2 elements, queue`() =
    runTest {
      val queue: Queue<Int> = ArrayDeque(listOf(4, 8))
      shouldThrow<IllegalArgumentException> {
        queue.singleNullOrThrow()
      }
    }

  @Test
  fun `with predicate, 0 matches, array`() =
    runTest {
      val array: Array<Int> = arrayOf(3, 4, 5)
      array.singleNullOrThrow { it > 5 }
        .shouldBeNull()
    }

  @Test
  fun `with predicate, 0 matches, list`() =
    runTest {
      val list: List<Int> = listOf(3, 4, 5)
      list.singleNullOrThrow { it > 5 }
        .shouldBeNull()
    }

  @Test
  fun `with predicate, 0 matches, queue`() =
    runTest {
      val queue: Queue<Int> = ArrayDeque(listOf(3, 4, 5))
      queue.singleNullOrThrow { it > 5 }
        .shouldBeNull()
    }

  @Test
  fun `with predicate, 1 match, array`() =
    runTest {
      val array: Array<Int> = arrayOf(3, 4, 5)
      array.singleNullOrThrow { it > 4 }
        .shouldBe(5)
    }

  @Test
  fun `with predicate, 1 match, list`() =
    runTest {
      val list: List<Int> = listOf(3, 4, 5)
      list.singleNullOrThrow { it > 4 }
        .shouldBe(5)
    }

  @Test
  fun `with predicate, 1 match, queue`() =
    runTest {
      val queue: Queue<Int> = ArrayDeque(listOf(3, 4, 5))
      queue.singleNullOrThrow { it > 4 }
        .shouldBe(5)
    }

  @Test
  fun `with predicate, 2 matches, array`() =
    runTest {
      val array: Array<Int> = arrayOf(3, 4, 5)
      shouldThrow<IllegalArgumentException> {
        array.singleNullOrThrow { it > 3 }
      }
    }

  @Test
  fun `with predicate, 2 matches, list`() =
    runTest {
      val list: List<Int> = listOf(3, 4, 5)
      shouldThrow<IllegalArgumentException> {
        list.singleNullOrThrow { it > 3 }
      }
    }

  @Test
  fun `with predicate, 2 matches, queue`() =
    runTest {
      val queue: Queue<Int> = ArrayDeque(listOf(3, 4, 5))
      shouldThrow<IllegalArgumentException> {
        queue.singleNullOrThrow { it > 3 }
      }
    }
}

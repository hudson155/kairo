@file:Suppress("InvalidPackageDeclaration")

package kotlin.collections

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KairoIterableTest {
  @Test
  fun `singleNullOrThrow (no predicate) - list`(): Unit = runTest {
    emptyList<Int>().singleNullOrThrow().shouldBeNull()
    listOf(1).singleNullOrThrow().shouldBe(1)
    shouldThrow<IllegalArgumentException> { listOf(1, 2).singleNullOrThrow() }
  }

  @Test
  fun `singleNullOrThrow (no predicate) - sequence iterable`(): Unit = runTest {
    emptySequence<Int>().asIterable().singleNullOrThrow().shouldBeNull()
    sequenceOf(1).asIterable().singleNullOrThrow().shouldBe(1)
    shouldThrow<IllegalArgumentException> { sequenceOf(1, 2).asIterable().singleNullOrThrow() }
  }

  @Test
  fun `singleNullOrThrow (with predicate) - list`(): Unit = runTest {
    listOf(1, 2).singleNullOrThrow { it < 1 }.shouldBeNull()
    listOf(1, 2).singleNullOrThrow { it < 2 }.shouldBe(1)
    shouldThrow<IllegalArgumentException> { listOf(1, 2).singleNullOrThrow { it < 3 } }
  }

  @Test
  fun `singleNullOrThrow (with predicate) - sequence iterable`(): Unit = runTest {
    sequenceOf(1, 2).asIterable().singleNullOrThrow { it < 1 }.shouldBeNull()
    sequenceOf(1, 2).asIterable().singleNullOrThrow { it < 2 }.shouldBe(1)
    shouldThrow<IllegalArgumentException> { sequenceOf(1, 2).asIterable().singleNullOrThrow { it < 3 } }
  }
}

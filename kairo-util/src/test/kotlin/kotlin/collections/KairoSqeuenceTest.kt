@file:Suppress("InvalidPackageDeclaration")

package kotlin.collections

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KairoSqeuenceTest {
  @Test
  fun `singleNullOrThrow (no predicate)`(): Unit = runTest {
    emptySequence<Int>().singleNullOrThrow().shouldBeNull()
    listOf(1).singleNullOrThrow().shouldBe(1)
    shouldThrow<IllegalArgumentException> { listOf(1, 2).singleNullOrThrow() }
  }

  @Test
  fun `singleNullOrThrow (with predicate)`(): Unit = runTest {
    sequenceOf(1, 2).singleNullOrThrow { it < 1 }.shouldBeNull()
    sequenceOf(1, 2).singleNullOrThrow { it < 2 }.shouldBe(1)
    shouldThrow<IllegalArgumentException> { listOf(1, 2).singleNullOrThrow { it < 3 } }
  }
}

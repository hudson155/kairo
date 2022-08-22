@file:Suppress("InvalidPackageDeclaration")

package kotlin

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class LimberCollectionsTest {
  @Test
  fun `singleNullOrThrow - list`() {
    emptyList<Int>().singleNullOrThrow().shouldBeNull()
    listOf(1).singleNullOrThrow().shouldBe(1)
    shouldThrow<IllegalArgumentException> { listOf(1, 2).singleNullOrThrow() }
  }

  @Test
  fun `singleNullOrThrow - sequence iterable`() {
    emptySequence<Int>().asIterable().singleNullOrThrow().shouldBeNull()
    sequenceOf(1).asIterable().singleNullOrThrow().shouldBe(1)
    shouldThrow<IllegalArgumentException> { sequenceOf(1, 2).asIterable().singleNullOrThrow() }
  }
}

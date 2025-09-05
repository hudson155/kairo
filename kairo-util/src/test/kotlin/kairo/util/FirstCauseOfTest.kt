package kairo.util

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("ThrowingExceptionsWithoutMessageOrCause")
internal class FirstCauseOfTest {
  @Test
  fun self(): Unit =
    runTest {
      var expected: IllegalArgumentException
      val e = IllegalArgumentException()
        .also { expected = it }
      e.firstCauseOf<IllegalArgumentException>()
        .shouldBe(expected)
    }

  @Test
  fun `direct parent (last)`(): Unit =
    runTest {
      var expected: IllegalArgumentException
      val e = IllegalArgumentException()
        .also { expected = it }
        .let { IllegalStateException(it) }
      e.firstCauseOf<IllegalArgumentException>()
        .shouldBe(expected)
    }

  @Test
  fun `direct parent (not last)`(): Unit =
    runTest {
      var expected: IllegalArgumentException
      val e = IllegalStateException()
        .let { IllegalArgumentException(it) }
        .also { expected = it }
        .let { IllegalStateException(it) }
      e.firstCauseOf<IllegalArgumentException>()
        .shouldBe(expected)
    }

  @Test
  fun `distant parent (last)`(): Unit =
    runTest {
      var expected: IllegalArgumentException
      val e = IllegalArgumentException()
        .also { expected = it }
        .let { IllegalStateException(it) }
        .let { IllegalStateException(it) }
        .let { IllegalStateException(it) }
        .let { IllegalStateException(it) }
      e.firstCauseOf<IllegalArgumentException>()
        .shouldBe(expected)
    }

  @Test
  fun `distant parent (not last)`(): Unit =
    runTest {
      var expected: IllegalArgumentException
      val e = IllegalStateException()
        .let { IllegalArgumentException(it) }
        .also { expected = it }
        .let { IllegalStateException(it) }
        .let { IllegalStateException(it) }
        .let { IllegalStateException(it) }
        .let { IllegalStateException(it) }
      e.firstCauseOf<IllegalArgumentException>()
        .shouldBe(expected)
    }

  @Test
  fun multiple(): Unit =
    runTest {
      var expected: IllegalArgumentException
      val e = IllegalStateException()
        .let { IllegalArgumentException(it) }
        .let { IllegalStateException(it) }
        .let { IllegalArgumentException(it) }
        .also { expected = it }
        .let { IllegalStateException(it) }
      e.firstCauseOf<IllegalArgumentException>()
        .shouldBe(expected)
    }

  @Test
  fun none(): Unit =
    runTest {
      val e = IllegalStateException()
        .let { IllegalStateException(it) }
        .let { IllegalStateException(it) }
      e.firstCauseOf<IllegalArgumentException>()
        .shouldBeNull()
    }
}

package kairo.closeable

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KairoCloseableTest {
  @Test
  fun close(): Unit = runTest {
    var isClosed = false
    val closeable = object : KairoCloseable {
      override fun close() {
        isClosed = true
      }
    }
    isClosed.shouldBeFalse()
    closeable.close()
    isClosed.shouldBeTrue()
  }

  @Test
  fun `use, no throw`(): Unit = runTest {
    var isClosed = false
    val closeable = object : KairoCloseable {
      override fun close() {
        isClosed = true
      }
    }
    isClosed.shouldBeFalse()
    shouldNotThrowAny {
      closeable.use {}
    }
    isClosed.shouldBeTrue()
  }

  @Test
  fun `use, throw`(): Unit = runTest {
    var isClosed = false
    val closeable = object : KairoCloseable {
      override fun close() {
        isClosed = true
      }
    }
    isClosed.shouldBeFalse()
    shouldThrow<IllegalStateException> {
      closeable.use {
        throw IllegalStateException()
      }
    }
    isClosed.shouldBeTrue()
  }
}

package kairo.reflect

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Tests [KairoType.from].
 */
internal class KairoTypeInferenceTest {
  internal abstract class AbstractExampleClass<T : Any> {
    val type: KairoType<T> = KairoType.from(AbstractExampleClass::class, 0, this::class)
  }

  internal class AbstractExampleIntSubclass : AbstractExampleClass<Int>()

  internal class AbstractExampleStringSubclass : AbstractExampleClass<String>()

  internal class AbstractExampleStringListSubclass : AbstractExampleClass<List<String>>()

  internal class ConcreteExampleClass<T : Any> {
    val type: KairoType<T> = KairoType.from(ConcreteExampleClass::class, 0, this::class)
  }

  @Test
  fun `abstract int subclass`(): Unit =
    runTest {
      AbstractExampleIntSubclass().type.shouldBe(kairoType<Int>())
    }

  @Test
  fun `abstract string subclass`(): Unit =
    runTest {
      AbstractExampleStringSubclass().type.shouldBe(kairoType<String>())
    }

  @Test
  fun `abstract string list subclass`(): Unit =
    runTest {
      AbstractExampleStringListSubclass().type.shouldBe(kairoType<List<String>>())
    }

  @Test
  fun `concrete int class`(): Unit =
    runTest {
      shouldThrow<NoSuchElementException> {
        ConcreteExampleClass<Int>()
      }
    }

  @Test
  fun `concrete string class`(): Unit =
    runTest {
      shouldThrow<NoSuchElementException> {
        ConcreteExampleClass<String>()
      }
    }

  @Test
  fun `concrete string list class`(): Unit =
    runTest {
      shouldThrow<NoSuchElementException> {
        ConcreteExampleClass<List<String>>()
      }
    }
}

package kairo.reflect

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class InferTypeTest {
  internal abstract class AbstractExampleClass<T : Any> {
    val kClass: KClass<T> = inferKClass(AbstractExampleClass::class, 0, this::class)
    val kType: KType = inferKType(AbstractExampleClass::class, 0, this::class)
  }

  internal class AbstractExampleIntSubclass : AbstractExampleClass<Int>()

  internal class AbstractExampleStringSubclass : AbstractExampleClass<String>()

  internal class AbstractExampleStringListSubclass : AbstractExampleClass<List<String>>()

  internal class ConcreteExampleClass<T : Any> {
    val kClass: KClass<T> = inferKClass(ConcreteExampleClass::class, 0, this::class)
    val kType: KType = inferKType(ConcreteExampleClass::class, 0, this::class)
  }

  @Test
  fun `abstract int subclass`() {
    runTest {
      with(AbstractExampleIntSubclass()) {
        kClass.shouldBe(Int::class)
        kType.shouldBe(typeOf<Int>())
      }
    }
  }

  @Test
  fun `abstract string subclass`() {
    runTest {
      with(AbstractExampleStringSubclass()) {
        kClass.shouldBe(String::class)
        kType.shouldBe(typeOf<String>())
      }
    }
  }

  @Test
  fun `abstract string list subclass`() {
    runTest {
      with(AbstractExampleStringListSubclass()) {
        kClass.shouldBe(List::class)
        kType.shouldBe(typeOf<List<String>>())
      }
    }

    @Test
    fun `concrete int class`() {
      runTest {
        shouldThrow<NoSuchElementException> {
          ConcreteExampleClass<Int>()
        }
      }
    }

    @Test
    fun `concrete string class`() {
      runTest {
        shouldThrow<NoSuchElementException> {
          ConcreteExampleClass<String>()
        }
      }
    }

    @Test
    fun `concrete string list class`() {
      runTest {
        shouldThrow<NoSuchElementException> {
          ConcreteExampleClass<List<String>>()
        }
      }
    }
  }
}

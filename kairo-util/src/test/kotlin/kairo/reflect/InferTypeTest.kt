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
    val kClass: KClass<T> = inferClass(AbstractExampleClass::class, 0, this::class)
    val type: KType = inferType(AbstractExampleClass::class, 0, this::class)
  }

  internal class AbstractExampleIntSubclass : AbstractExampleClass<Int>()

  internal class AbstractExampleStringSubclass : AbstractExampleClass<String>()

  internal class AbstractExampleStringListSubclass : AbstractExampleClass<List<String>>()

  internal class ConcreteExampleClass<T : Any> {
    val kClass: KClass<T> = inferClass(ConcreteExampleClass::class, 0, this::class)
    val type: KType = inferType(ConcreteExampleClass::class, 0, this::class)
  }

  @Test
  fun `abstract int subclass`(): Unit =
    runTest {
      with(AbstractExampleIntSubclass()) {
        kClass.shouldBe(Int::class)
        type.shouldBe(typeOf<Int>())
      }
    }

  @Test
  fun `abstract string subclass`(): Unit =
    runTest {
      with(AbstractExampleStringSubclass()) {
        kClass.shouldBe(String::class)
        type.shouldBe(typeOf<String>())
      }
    }

  @Test
  fun `abstract string list subclass`(): Unit =
    runTest {
      with(AbstractExampleStringListSubclass()) {
        kClass.shouldBe(List::class)
        type.shouldBe(typeOf<List<String>>())
      }
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

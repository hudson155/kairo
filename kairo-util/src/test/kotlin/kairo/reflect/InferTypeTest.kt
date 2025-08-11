package kairo.reflect

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf
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
  fun `abstract int subclass`() {
    with(AbstractExampleIntSubclass()) {
      kClass.shouldBe(Int::class)
      type.shouldBe(typeOf<Int>())
    }
  }

  @Test
  fun `abstract string subclass`() {
    with(AbstractExampleStringSubclass()) {
      kClass.shouldBe(String::class)
      type.shouldBe(typeOf<String>())
    }
  }

  @Test
  fun `abstract string list subclass`() {
    with(AbstractExampleStringListSubclass()) {
      kClass.shouldBe(List::class)
      type.shouldBe(typeOf<List<String>>())
    }
  }

  @Test
  fun `concrete int class`() {
    shouldThrow<NoSuchElementException> {
      ConcreteExampleClass<Int>()
    }
  }

  @Test
  fun `concrete string class`() {
    shouldThrow<NoSuchElementException> {
      ConcreteExampleClass<String>()
    }
  }

  @Test
  fun `concrete string list class`() {
    shouldThrow<NoSuchElementException> {
      ConcreteExampleClass<List<String>>()
    }
  }
}

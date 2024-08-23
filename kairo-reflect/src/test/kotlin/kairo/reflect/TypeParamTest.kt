package kairo.reflect

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlin.reflect.KClass
import org.junit.jupiter.api.Test

internal class TypeParamTest {
  internal abstract class AbstractExampleClass<T : Any> {
    val typeParam: KClass<T> = typeParam(AbstractExampleClass::class, 0, this::class)
  }

  internal class AbstractExampleIntSubclass : AbstractExampleClass<Int>()

  internal class AbstractExampleStringSubclass : AbstractExampleClass<String>()

  internal class ConcreteExampleClass<T : Any> {
    val typeParam: KClass<T> = typeParam(ConcreteExampleClass::class, 0, this::class)
  }

  @Test
  fun `abstract int subclass`() {
    AbstractExampleIntSubclass().typeParam.shouldBe(Int::class)
  }

  @Test
  fun `abstract string subclass`() {
    AbstractExampleStringSubclass().typeParam.shouldBe(String::class)
  }

  @Test
  fun `concrete int class`() {
    shouldThrow<NullPointerException> {
      ConcreteExampleClass<Int>()
    }
  }

  @Test
  fun `concrete string class`() {
    shouldThrow<NullPointerException> {
      ConcreteExampleClass<String>()
    }
  }
}

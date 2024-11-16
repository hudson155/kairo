package kairo.reflect

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlin.reflect.KClass
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class TypeParamKClassTest {
  internal abstract class AbstractExampleClass<T : Any> {
    val typeParam: KClass<T> = typeParamKclass(AbstractExampleClass::class, 0, this::class)
  }

  internal class AbstractExampleIntSubclass : AbstractExampleClass<Int>()

  internal class AbstractExampleStringSubclass : AbstractExampleClass<String>()

  internal class AbstractExampleStringListSubclass : AbstractExampleClass<List<String>>()

  internal class ConcreteExampleClass<T : Any> {
    val typeParam: KClass<T> = typeParamKclass(ConcreteExampleClass::class, 0, this::class)
  }

  @Test
  fun `abstract int subclass`(): Unit = runTest {
    AbstractExampleIntSubclass().typeParam.shouldBe(Int::class)
  }

  @Test
  fun `abstract string subclass`(): Unit = runTest {
    AbstractExampleStringSubclass().typeParam.shouldBe(String::class)
  }

  @Test
  fun `abstract string list subclass`(): Unit = runTest {
    shouldThrow<ClassCastException> {
      AbstractExampleStringListSubclass().typeParam.shouldBe(String::class)
    }
  }

  @Test
  fun `concrete int class`(): Unit = runTest {
    shouldThrow<NullPointerException> {
      ConcreteExampleClass<Int>()
    }
  }

  @Test
  fun `concrete string class`(): Unit = runTest {
    shouldThrow<NullPointerException> {
      ConcreteExampleClass<String>()
    }
  }

  @Test
  fun `concrete string list class`(): Unit = runTest {
    shouldThrow<NullPointerException> {
      ConcreteExampleClass<List<String>>()
    }
  }
}
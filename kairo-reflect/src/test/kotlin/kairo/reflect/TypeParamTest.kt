package kairo.reflect

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.lang.reflect.Type
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class TypeParamTest {
  internal abstract class AbstractExampleClass<T : Any> {
    val typeParam: Type = typeParam(AbstractExampleClass::class, 0, this::class)
  }

  internal class AbstractExampleIntSubclass : AbstractExampleClass<Int>()

  internal class AbstractExampleStringSubclass : AbstractExampleClass<String>()

  internal class AbstractExampleStringListSubclass : AbstractExampleClass<List<String>>()

  internal class ConcreteExampleClass<T : Any> {
    val typeParam: Type = typeParam(ConcreteExampleClass::class, 0, this::class)
  }

  @Test
  fun `abstract int subclass`(): Unit = runTest {
    AbstractExampleIntSubclass().typeParam.should { typeParam ->
      typeParam.typeName.shouldBe("java.lang.Integer")
    }
  }

  @Test
  fun `abstract string subclass`(): Unit = runTest {
    AbstractExampleStringSubclass().typeParam.should { typeParam ->
      typeParam.typeName.shouldBe("java.lang.String")
    }
  }

  @Test
  fun `abstract string list subclass`(): Unit = runTest {
    AbstractExampleStringListSubclass().typeParam.should { typeParam ->
      typeParam.typeName.shouldBe("java.util.List<? extends java.lang.String>")
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

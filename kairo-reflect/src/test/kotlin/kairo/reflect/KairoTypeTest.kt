package kairo.reflect

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlin.reflect.typeOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KairoTypeTest {
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
      AbstractExampleIntSubclass().type.should { type ->
        type.kotlinType.shouldBe(typeOf<Int>())
        type.javaType.typeName.shouldBe("java.lang.Integer")
        type.kotlinClass.shouldBe(Int::class)
        type.javaClass.shouldBe(Int::class.java)
      }
    }

  @Test
  fun `abstract string subclass`(): Unit =
    runTest {
      AbstractExampleStringSubclass().type.should { type ->
        type.kotlinType.shouldBe(typeOf<String>())
        type.javaType.typeName.shouldBe("java.lang.String")
        type.kotlinClass.shouldBe(String::class)
        type.javaClass.shouldBe(String::class.java)
      }
    }

  @Test
  fun `abstract string list subclass`(): Unit =
    runTest {
      AbstractExampleStringListSubclass().type.should { type ->
        type.kotlinType.shouldBe(typeOf<List<String>>())
        type.javaType.typeName.shouldBe("java.util.List<? extends java.lang.String>")
        type.kotlinClass.shouldBe(List::class)
        type.javaClass.shouldBe(List::class.java)
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

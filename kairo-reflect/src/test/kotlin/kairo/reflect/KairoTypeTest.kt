package kairo.reflect

import io.kotest.matchers.shouldBe
import kotlin.reflect.typeOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KairoTypeTest {
  @Test
  fun int(): Unit =
    runTest {
      val type = kairoType<Int>()
      type.kotlinType.shouldBe(typeOf<Int>())
      type.javaType.typeName.shouldBe("int")
      type.kotlinClass.shouldBe(Int::class)
      type.javaClass.shouldBe(Int::class.java)
    }

  @Test
  fun string(): Unit =
    runTest {
      val type = kairoType<String>()
      type.kotlinType.shouldBe(typeOf<String>())
      type.javaType.typeName.shouldBe("java.lang.String")
      type.kotlinClass.shouldBe(String::class)
      type.javaClass.shouldBe(String::class.java)
    }
}

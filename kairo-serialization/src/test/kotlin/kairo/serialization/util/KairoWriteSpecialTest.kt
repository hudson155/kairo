package kairo.serialization.util

import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.matchers.shouldBe
import kairo.reflect.kairoType
import kairo.serialization.jsonMapper
import kairo.serialization.typeReference
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KairoWriteSpecialTest {
  internal data class MyClass(
    val foo: String,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `data class, inline`(): Unit = runTest {
    mapper.kairoWriteSpecial<MyClass>(MyClass("bar"))
      .shouldBe("{\"foo\":\"bar\"}")
  }

  @Test
  fun `data class, kairo type`(): Unit = runTest {
    mapper.kairoWriteSpecial(MyClass("bar"), kairoType<MyClass>())
      .shouldBe("{\"foo\":\"bar\"}")
  }

  @Test
  fun `data class, type reference`(): Unit = runTest {
    mapper.kairoWriteSpecial(MyClass("bar"), kairoType<MyClass>().typeReference)
      .shouldBe("{\"foo\":\"bar\"}")
  }

  @Test
  fun `data class, java type`(): Unit = runTest {
    mapper.kairoWriteSpecial(MyClass("bar"), mapper.constructType(kairoType<MyClass>().typeReference))
      .shouldBe("{\"foo\":\"bar\"}")
  }

  @Test
  fun `string, inline`(): Unit = runTest {
    mapper.kairoWriteSpecial<String>("foo")
      .shouldBe("foo")
  }

  @Test
  fun `string, kairo type`(): Unit = runTest {
    mapper.kairoWriteSpecial("foo", kairoType<String>())
      .shouldBe("foo")
  }

  @Test
  fun `string, type reference`(): Unit = runTest {
    mapper.kairoWriteSpecial("foo", kairoType<String>().typeReference)
      .shouldBe("foo")
  }

  @Test
  fun `string, java type`(): Unit = runTest {
    mapper.kairoWriteSpecial("foo", mapper.constructType(kairoType<String>().typeReference))
      .shouldBe("foo")
  }

  @Test
  fun `boolean, inline`(): Unit = runTest {
    mapper.kairoWriteSpecial<Boolean>(true)
      .shouldBe("true")
  }

  @Test
  fun `boolean, kairo type`(): Unit = runTest {
    mapper.kairoWriteSpecial(true, kairoType<Boolean>())
      .shouldBe("true")
  }

  @Test
  fun `boolean, type reference`(): Unit = runTest {
    mapper.kairoWriteSpecial(true, kairoType<Boolean>().typeReference)
      .shouldBe("true")
  }

  @Test
  fun `boolean, java type`(): Unit = runTest {
    mapper.kairoWriteSpecial(true, mapper.constructType(kairoType<Boolean>().typeReference))
      .shouldBe("true")
  }
}

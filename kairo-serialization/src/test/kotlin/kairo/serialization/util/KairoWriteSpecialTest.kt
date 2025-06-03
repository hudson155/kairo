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
  fun `data class, inline, non-null`(): Unit = runTest {
    mapper.kairoWriteSpecial<MyClass>(MyClass("bar"))
      .shouldBe("{\"foo\":\"bar\"}")
  }

  @Test
  fun `data class, inline, null`(): Unit = runTest {
    mapper.kairoWriteSpecial<MyClass>(null)
      .shouldBe("null")
  }

  @Test
  fun `data class, kairo type, non-null`(): Unit = runTest {
    mapper.kairoWriteSpecial(MyClass("bar"), kairoType<MyClass>())
      .shouldBe("{\"foo\":\"bar\"}")
  }

  @Test
  fun `data class, kairo type, null`(): Unit = runTest {
    mapper.kairoWriteSpecial(null, kairoType<MyClass>())
      .shouldBe("null")
  }

  @Test
  fun `data class, type reference, non-null`(): Unit = runTest {
    mapper.kairoWriteSpecial(MyClass("bar"), kairoType<MyClass>().typeReference)
      .shouldBe("{\"foo\":\"bar\"}")
  }

  @Test
  fun `data class, type reference, null`(): Unit = runTest {
    mapper.kairoWriteSpecial(null, kairoType<MyClass>().typeReference)
      .shouldBe("null")
  }

  @Test
  fun `data class, java type, non-null`(): Unit = runTest {
    mapper.kairoWriteSpecial(MyClass("bar"), mapper.constructType(kairoType<MyClass>().typeReference))
      .shouldBe("{\"foo\":\"bar\"}")
  }

  @Test
  fun `data class, java type, null`(): Unit = runTest {
    mapper.kairoWriteSpecial(null, mapper.constructType(kairoType<MyClass>().typeReference))
      .shouldBe("null")
  }

  @Test
  fun `string, inline, non-null`(): Unit = runTest {
    mapper.kairoWriteSpecial<String>("foo")
      .shouldBe("foo")
  }

  @Test
  fun `string, inline, null`(): Unit = runTest {
    mapper.kairoWriteSpecial<String>(null)
      .shouldBe("null")
  }

  @Test
  fun `string, kairo type, non-null`(): Unit = runTest {
    mapper.kairoWriteSpecial("foo", kairoType<String>())
      .shouldBe("foo")
  }

  @Test
  fun `string, kairo type, null`(): Unit = runTest {
    mapper.kairoWriteSpecial(null, kairoType<String>())
      .shouldBe("null")
  }

  @Test
  fun `string, type reference, non-null`(): Unit = runTest {
    mapper.kairoWriteSpecial("foo", kairoType<String>().typeReference)
      .shouldBe("foo")
  }

  @Test
  fun `string, type reference, null`(): Unit = runTest {
    mapper.kairoWriteSpecial(null, kairoType<String>().typeReference)
      .shouldBe("null")
  }

  @Test
  fun `string, java type, non-null`(): Unit = runTest {
    mapper.kairoWriteSpecial("foo", mapper.constructType(kairoType<String>().typeReference))
      .shouldBe("foo")
  }

  @Test
  fun `string, java type, null`(): Unit = runTest {
    mapper.kairoWriteSpecial(null, mapper.constructType(kairoType<String>().typeReference))
      .shouldBe("null")
  }

  @Test
  fun `boolean, inline, non-null`(): Unit = runTest {
    mapper.kairoWriteSpecial<Boolean>(true)
      .shouldBe("true")
  }

  @Test
  fun `boolean, inline, null`(): Unit = runTest {
    mapper.kairoWriteSpecial<Boolean>(null)
      .shouldBe("null")
  }

  @Test
  fun `boolean, kairo type, non-null`(): Unit = runTest {
    mapper.kairoWriteSpecial(true, kairoType<Boolean>())
      .shouldBe("true")
  }

  @Test
  fun `boolean, kairo type, null`(): Unit = runTest {
    mapper.kairoWriteSpecial(null, kairoType<Boolean>())
      .shouldBe("null")
  }

  @Test
  fun `boolean, type reference, non-null`(): Unit = runTest {
    mapper.kairoWriteSpecial(true, kairoType<Boolean>().typeReference)
      .shouldBe("true")
  }

  @Test
  fun `boolean, type reference, null`(): Unit = runTest {
    mapper.kairoWriteSpecial(null, kairoType<Boolean>().typeReference)
      .shouldBe("null")
  }

  @Test
  fun `boolean, java type, non-null`(): Unit = runTest {
    mapper.kairoWriteSpecial(true, mapper.constructType(kairoType<Boolean>().typeReference))
      .shouldBe("true")
  }

  @Test
  fun `boolean, java type, null`(): Unit = runTest {
    mapper.kairoWriteSpecial(null, mapper.constructType(kairoType<Boolean>().typeReference))
      .shouldBe("null")
  }
}

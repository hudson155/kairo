package kairo.serialization.util

import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kairo.reflect.kairoType
import kairo.serialization.jsonMapper
import kairo.serialization.typeReference
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KairoReadSpecialTest {
  internal data class MyClass(
    val foo: String,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `data class, inline, non-null`(): Unit = runTest {
    mapper.kairoReadSpecial<MyClass>("{ \"foo\": \"bar\"}")
      .shouldBe(MyClass("bar"))
  }

  @Test
  fun `data class, inline, null`(): Unit = runTest {
    mapper.kairoReadSpecial<MyClass>("null")
      .shouldBeNull()
  }

  @Test
  fun `data class, kairo type, non-null`(): Unit = runTest {
    mapper.kairoReadSpecial("{ \"foo\": \"bar\"}", kairoType<MyClass>())
      .shouldBe(MyClass("bar"))
  }

  @Test
  fun `data class, kairo type, null`(): Unit = runTest {
    mapper.kairoReadSpecial("null", kairoType<MyClass>())
      .shouldBeNull()
  }

  @Test
  fun `data class, type reference, non-null`(): Unit = runTest {
    mapper.kairoReadSpecial("{ \"foo\": \"bar\"}", kairoType<MyClass>().typeReference)
      .shouldBe(MyClass("bar"))
  }

  @Test
  fun `data class, type reference, null`(): Unit = runTest {
    mapper.kairoReadSpecial("null", kairoType<MyClass>().typeReference)
      .shouldBeNull()
  }

  @Test
  fun `data class, java type, non-null`(): Unit = runTest {
    mapper.kairoReadSpecial("{ \"foo\": \"bar\"}", mapper.constructType(kairoType<MyClass>().typeReference))
      .shouldBe(MyClass("bar"))
  }

  @Test
  fun `data class, java type, null`(): Unit = runTest {
    mapper.kairoReadSpecial("null", mapper.constructType(kairoType<MyClass>().typeReference))
      .shouldBeNull()
  }

  @Test
  fun `string, inline, non-null`(): Unit = runTest {
    mapper.kairoReadSpecial<String>("foo")
      .shouldBe("foo")
  }

  @Test
  fun `string, inline, null`(): Unit = runTest {
    mapper.kairoReadSpecial<String>("null")
      .shouldBeNull()
  }

  @Test
  fun `string, kairo type, non-null`(): Unit = runTest {
    mapper.kairoReadSpecial("foo", kairoType<String>())
      .shouldBe("foo")
  }

  @Test
  fun `string, kairo type, null`(): Unit = runTest {
    mapper.kairoReadSpecial("null", kairoType<String>())
      .shouldBeNull()
  }

  @Test
  fun `string, type reference, non-null`(): Unit = runTest {
    mapper.kairoReadSpecial("foo", kairoType<String>().typeReference)
      .shouldBe("foo")
  }

  @Test
  fun `string, type reference, null`(): Unit = runTest {
    mapper.kairoReadSpecial("null", kairoType<String>().typeReference)
      .shouldBeNull()
  }

  @Test
  fun `string, java type, non-null`(): Unit = runTest {
    mapper.kairoReadSpecial("foo", mapper.constructType(kairoType<String>().typeReference))
      .shouldBe("foo")
  }

  @Test
  fun `string, java type, null`(): Unit = runTest {
    mapper.kairoReadSpecial("null", mapper.constructType(kairoType<String>().typeReference))
      .shouldBeNull()
  }

  @Test
  fun `boolean, inline, non-null`(): Unit = runTest {
    mapper.kairoReadSpecial<Boolean>("true")
      .shouldBe(true)
  }

  @Test
  fun `boolean, inline, null`(): Unit = runTest {
    mapper.kairoReadSpecial<Boolean>("null")
      .shouldBeNull()
  }

  @Test
  fun `boolean, kairo type, non-null`(): Unit = runTest {
    mapper.kairoReadSpecial("true", kairoType<Boolean>())
      .shouldBe(true)
  }

  @Test
  fun `boolean, kairo type, null`(): Unit = runTest {
    mapper.kairoReadSpecial("null", kairoType<Boolean>())
      .shouldBeNull()
  }

  @Test
  fun `boolean, type reference, non-null`(): Unit = runTest {
    mapper.kairoReadSpecial("true", kairoType<Boolean>().typeReference)
      .shouldBe(true)
  }

  @Test
  fun `boolean, type reference, null`(): Unit = runTest {
    mapper.kairoReadSpecial("null", kairoType<Boolean>().typeReference)
      .shouldBeNull()
  }

  @Test
  fun `boolean, java type, non-null`(): Unit = runTest {
    mapper.kairoReadSpecial("true", mapper.constructType(kairoType<Boolean>().typeReference))
      .shouldBe(true)
  }

  @Test
  fun `boolean, java type, null`(): Unit = runTest {
    mapper.kairoReadSpecial("null", mapper.constructType(kairoType<Boolean>().typeReference))
      .shouldBeNull()
  }
}

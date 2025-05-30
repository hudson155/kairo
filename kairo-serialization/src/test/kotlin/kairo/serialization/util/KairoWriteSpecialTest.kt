package kairo.serialization.util

import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.matchers.shouldBe
import kairo.reflect.kairoType
import kairo.serialization.jsonMapper
import kairo.serialization.typeReference
import org.junit.jupiter.api.Test

internal class KairoWriteSpecialTest {
  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `string, inline`() {
    mapper.kairoWriteSpecial<String>("foo")
      .shouldBe("foo")
  }

  @Test
  fun `string, kairo type`() {
    mapper.kairoWriteSpecial("foo", kairoType<String>())
      .shouldBe("foo")
  }

  @Test
  fun `string, type reference`() {
    mapper.kairoWriteSpecial("foo", kairoType<String>().typeReference)
      .shouldBe("foo")
  }

  @Test
  fun `string, java type`() {
    mapper.kairoWriteSpecial("foo", mapper.constructType(kairoType<String>().typeReference))
      .shouldBe("foo")
  }

  @Test
  fun `boolean, inline`() {
    mapper.kairoWriteSpecial<Boolean>(true)
      .shouldBe("true")
  }

  @Test
  fun `boolean, kairo type`() {
    mapper.kairoWriteSpecial(true, kairoType<Boolean>())
      .shouldBe("true")
  }

  @Test
  fun `boolean, type reference`() {
    mapper.kairoWriteSpecial(true, kairoType<Boolean>().typeReference)
      .shouldBe("true")
  }

  @Test
  fun `boolean, java type`() {
    mapper.kairoWriteSpecial(true, mapper.constructType(kairoType<Boolean>().typeReference))
      .shouldBe("true")
  }

  @Test
  fun `null, inline`() {
    mapper.kairoWriteSpecial<Boolean>(null)
      .shouldBe("null")
  }

  @Test
  fun `null, kairo type`() {
    mapper.kairoWriteSpecial(null, kairoType<Boolean>())
      .shouldBe("null")
  }

  @Test
  fun `null, type reference`() {
    mapper.kairoWriteSpecial(null, kairoType<Boolean>().typeReference)
      .shouldBe("null")
  }

  @Test
  fun `null, java type`() {
    mapper.kairoWriteSpecial(null, mapper.constructType(kairoType<Boolean>().typeReference))
      .shouldBe("null")
  }
}

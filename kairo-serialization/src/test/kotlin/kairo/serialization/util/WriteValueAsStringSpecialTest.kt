package kairo.serialization.util

import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.matchers.shouldBe
import kairo.reflect.kairoType
import kairo.serialization.jsonMapper
import kairo.serialization.typeReference
import org.junit.jupiter.api.Test

internal class WriteValueAsStringSpecialTest {
  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `string, inline`() {
    mapper.writeValueAsStringSpecial<String>("foo")
      .shouldBe("foo")
  }

  @Test
  fun `string, kairo type`() {
    mapper.writeValueAsStringSpecial("foo", kairoType<String>())
      .shouldBe("foo")
  }

  @Test
  fun `string, type reference`() {
    mapper.writeValueAsStringSpecial("foo", kairoType<String>().typeReference)
      .shouldBe("foo")
  }

  @Test
  fun `string, java type`() {
    mapper.writeValueAsStringSpecial("foo", mapper.constructType(kairoType<String>().typeReference))
      .shouldBe("foo")
  }

  @Test
  fun `boolean, inline`() {
    mapper.writeValueAsStringSpecial<Boolean>(true)
      .shouldBe("true")
  }

  @Test
  fun `boolean, kairo type`() {
    mapper.writeValueAsStringSpecial(true, kairoType<Boolean>())
      .shouldBe("true")
  }

  @Test
  fun `boolean, type reference`() {
    mapper.writeValueAsStringSpecial(true, kairoType<Boolean>().typeReference)
      .shouldBe("true")
  }

  @Test
  fun `boolean, java type`() {
    mapper.writeValueAsStringSpecial(true, mapper.constructType(kairoType<Boolean>().typeReference))
      .shouldBe("true")
  }

  @Test
  fun `null, inline`() {
    mapper.writeValueAsStringSpecial<Boolean>(null)
      .shouldBe("null")
  }

  @Test
  fun `null, kairo type`() {
    mapper.writeValueAsStringSpecial(null, kairoType<Boolean>())
      .shouldBe("null")
  }

  @Test
  fun `null, type reference`() {
    mapper.writeValueAsStringSpecial(null, kairoType<Boolean>().typeReference)
      .shouldBe("null")
  }

  @Test
  fun `null, java type`() {
    mapper.writeValueAsStringSpecial(null, mapper.constructType(kairoType<Boolean>().typeReference))
      .shouldBe("null")
  }
}

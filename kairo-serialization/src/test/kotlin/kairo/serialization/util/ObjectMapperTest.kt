package kairo.serialization.util

import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import org.junit.jupiter.api.Test

internal class ObjectMapperTest {
  private val mapper: JsonMapper = jsonMapper()

  @Test
  fun `readValueSpecial, string`() {
    mapper.readValueSpecial("foo", mapper.constructType(String::class.java))
      .shouldBe("foo")
  }

  @Test
  fun `readValueSpecial, boolean`() {
    mapper.readValueSpecial("true", mapper.constructType(Boolean::class.java))
      .shouldBe(true)
  }

  @Test
  fun `writeValueAsStringSpecial, string`() {
    mapper.writeValueAsStringSpecial("foo")
      .shouldBe("foo")
  }

  @Test
  fun `writeValueAsStringSpecial, boolean`() {
    mapper.writeValueAsStringSpecial("true")
      .shouldBe("true")
  }
}

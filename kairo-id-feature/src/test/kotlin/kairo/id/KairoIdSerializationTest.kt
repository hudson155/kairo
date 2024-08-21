package kairo.id

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.ObjectMapperFormat
import org.junit.jupiter.api.Test

internal class KairoIdSerializationTest {
  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun serialize() {
    mapper.writeValueAsString(KairoId("library_book", "2eDS1sMt"))
      .shouldBe("\"library_book_2eDS1sMt\"")
  }

  @Test
  fun deserialize() {
    mapper.readValue<KairoId>("\"library_book_2eDS1sMt\"")
      .shouldBe(KairoId("library_book", "2eDS1sMt"))
  }
}

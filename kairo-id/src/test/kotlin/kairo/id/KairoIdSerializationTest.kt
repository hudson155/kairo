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
    mapper.writeValueAsString(KairoId("library_book", "0")).shouldBe("\"library_book_0\"")
  }

  @Test
  fun deserialize() {
    mapper.readValue<KairoId>("\"library_book_0\"").shouldBe(KairoId("library_book", "0"))
  }
}

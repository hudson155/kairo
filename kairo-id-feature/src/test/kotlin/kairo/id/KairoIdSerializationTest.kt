package kairo.id

import com.fasterxml.jackson.databind.json.JsonMapper
import io.github.projectmapk.jackson.module.kogera.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KairoIdSerializationTest {
  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun serialize(): Unit = runTest {
    mapper.writeValueAsString(KairoId("library_book", "2eDS1sMt"))
      .shouldBe("\"library_book_2eDS1sMt\"")
  }

  @Test
  fun deserialize(): Unit = runTest {
    mapper.readValue<KairoId>("\"library_book_2eDS1sMt\"")
      .shouldBe(KairoId("library_book", "2eDS1sMt"))
  }
}

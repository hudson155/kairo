package kairo.doNotLogString

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class DoNotLogStringSerializationTest {
  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun serialize(): Unit = runTest {
    mapper.writeValueAsString(DoNotLogString("1")).shouldBe("\"1\"")
  }

  @Test
  fun deserialize(): Unit = runTest {
    mapper.readValue<DoNotLogString>("\"1\"").shouldBe(DoNotLogString("1"))
  }
}

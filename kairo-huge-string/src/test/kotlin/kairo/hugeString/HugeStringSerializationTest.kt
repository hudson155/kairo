package kairo.hugeString

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class HugeStringSerializationTest {
  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun serialize(): Unit = runTest {
    mapper.writeValueAsString(HugeString("1")).shouldBe("\"1\"")
  }

  @Test
  fun deserialize(): Unit = runTest {
    mapper.readValue<HugeString>("\"1\"").shouldBe(HugeString("1"))
  }
}

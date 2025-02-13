package kairo.protectedString

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ProtectedString.Access::class)
internal class ProtectedStringSerializationTest {
  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun serialize(): Unit = runTest {
    mapper.writeValueAsString(ProtectedString("1")).shouldBe("\"1\"")
  }

  @Test
  fun deserialize(): Unit = runTest {
    mapper.readValue<ProtectedString>("\"1\"").shouldBe(ProtectedString("1"))
  }
}

package kairo.doNotLogString

import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.util.kairoRead
import kairo.serialization.util.kairoWrite
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class DoNotLogStringSerializationTest {
  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun serialize(): Unit = runTest {
    mapper.kairoWrite(DoNotLogString("1")).shouldBe("\"1\"")
  }

  @Test
  fun deserialize(): Unit = runTest {
    mapper.kairoRead<DoNotLogString>("\"1\"").shouldBe(DoNotLogString("1"))
  }
}

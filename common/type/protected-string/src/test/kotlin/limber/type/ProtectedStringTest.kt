package limber.type

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import limber.serialization.ObjectMapperFactory
import org.junit.jupiter.api.Test

internal class ProtectedStringTest {
  private val objectMapper: ObjectMapper = ObjectMapperFactory.builder(ObjectMapperFactory.Format.Json).build()

  @Test
  fun `equals method`() {
    ProtectedString("1").shouldBe(ProtectedString("1"))
    ProtectedString("1").shouldNotBe(ProtectedString("2"))
  }

  @Test
  fun `hashCode method`() {
    ProtectedString("1").hashCode().shouldBe("1".hashCode())
    ProtectedString("1").hashCode().shouldNotBe("2".hashCode())
  }

  @Test
  fun `toString method`() {
    ProtectedString("1").toString().shouldBe("REDACTED")
  }

  @Test
  fun serialize() {
    objectMapper.writeValueAsString(ProtectedString("1")).shouldBe("\"1\"")
  }

  @Test
  fun deserialize() {
    objectMapper.readValue<ProtectedString>("\"1\"").shouldBe(ProtectedString("1"))
  }
}

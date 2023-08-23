package limber.auth

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import limber.serialization.ObjectMapperFactory
import org.junit.jupiter.api.Test

internal class PermissionKeyTest {
  @Suppress("EnumEntryName", "EnumEntryNameCase", "EnumNaming")
  @JsonDeserialize(using = TestPermission.Deserializer::class)
  enum class TestPermission {
    OrganizationAuth_Read,
    Report_UpdateOwn,
    ;

    @JsonValue
    internal val value: String = permissionKey(this)

    /**
     * Uses [TestPermission]'s [value] field to deserialize.
     *
     * Gracefully fails by returning null if the permission is unknown.
     */
    internal class Deserializer : StdDeserializer<TestPermission>(TestPermission::class.java) {
      private val byValue = TestPermission.entries.associateBy { it.value }

      override fun deserialize(p: JsonParser, ctxt: DeserializationContext): TestPermission? {
        val string = p.readValueAs(String::class.java) ?: return null
        return byValue[string]
      }
    }
  }

  private val objectMapper: ObjectMapper = ObjectMapperFactory.builder(ObjectMapperFactory.Format.Json).build()

  @Test
  fun serialize() {
    objectMapper.writeValueAsString(TestPermission.OrganizationAuth_Read)
      .shouldBe("\"organizationAuth:read\"")
    objectMapper.writeValueAsString(TestPermission.Report_UpdateOwn)
      .shouldBe("\"report:updateOwn\"")
  }

  @Test
  fun deserialize() {
    objectMapper.readValue<TestPermission>("\"organizationAuth:read\"")
      .shouldBe(TestPermission.OrganizationAuth_Read)
    objectMapper.readValue<TestPermission>("\"report:updateOwn\"")
      .shouldBe(TestPermission.Report_UpdateOwn)
  }
}

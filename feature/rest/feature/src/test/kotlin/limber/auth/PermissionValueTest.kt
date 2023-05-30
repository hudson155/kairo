package limber.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import limber.serialization.ObjectMapperFactory
import org.junit.jupiter.api.Test

internal class PermissionValueTest {
  private val objectMapper: ObjectMapper = ObjectMapperFactory.builder(ObjectMapperFactory.Format.Json).build()

  @Test
  fun `all, serialize`() {
    objectMapper.writeValueAsString(PermissionValue.All)
      .shouldBe("\"*\"")
  }

  @Test
  fun `all, deserialize`() {
    objectMapper.readValue<PermissionValue>("\"*\"")
      .shouldBe(PermissionValue.All)
  }

  @Test
  fun `some, serialize`() {
    objectMapper.writeValueAsString(PermissionValue.Some(emptySet()))
      .shouldBe("[]")

    val ids = setOf("id_0", "id_1")
    objectMapper.writeValueAsString(PermissionValue.Some(ids))
      .shouldBe("[${ids.joinToString(",") { "\"$it\"" }}]")
  }

  @Test
  fun `some, deserialize`() {
    objectMapper.readValue<PermissionValue>("[]")
      .shouldBe(PermissionValue.Some(emptySet()))

    val ids = setOf("id_0", "id_1")
    objectMapper.readValue<PermissionValue>("[${ids.joinToString(",") { "\"$it\"" }}]")
      .shouldBe(PermissionValue.Some(ids))
  }
}

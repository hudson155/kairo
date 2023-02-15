package limber.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import limber.serialization.ObjectMapperFactory
import org.junit.jupiter.api.Test
import java.util.UUID

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

    val guids = setOf(UUID.randomUUID(), UUID.randomUUID())
    objectMapper.writeValueAsString(PermissionValue.Some(guids))
      .shouldBe("[${guids.joinToString(",") { "\"$it\"" }}]")
  }

  @Test
  fun `some, deserialize`() {
    objectMapper.readValue<PermissionValue>("[]")
      .shouldBe(PermissionValue.Some(emptySet()))

    val guids = setOf(UUID.randomUUID(), UUID.randomUUID())
    objectMapper.readValue<PermissionValue>("[${guids.joinToString(",") { "\"$it\"" }}]")
      .shouldBe(PermissionValue.Some(guids))
  }
}

package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import java.util.UUID
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to UUID serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to UUIDs.
 */
internal class UuidNullableObjectMapperTest {
  /**
   * This test is specifically for nullable properties.
   */
  internal data class MyClass(
    val value: UUID?,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `serialize, default`() {
    mapper.writeValueAsString(MyClass(UUID.fromString("3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8")))
      .shouldBe("{\"value\":\"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8\"}")
  }

  @Test
  fun `serialize, null`() {
    mapper.writeValueAsString(MyClass(null))
      .shouldBe("{\"value\":null}")
  }

  @Test
  fun `deserialize, default`() {
    mapper.readValue<MyClass>("{ \"value\": \"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8\" }")
      .shouldBe(MyClass(UUID.fromString("3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8")))
  }

  @Test
  fun `deserialize, null`() {
    mapper.readValue<MyClass>("{ \"value\": null }")
      .shouldBe(MyClass(null))
  }

  @Test
  fun `deserialize, missing`() {
    mapper.readValue<MyClass>("{}").shouldBe(MyClass(null))
  }
}

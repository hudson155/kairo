package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class DataObjectSerializationTest {
  private val json: KairoJson =
    KairoJson {
      pretty = true
    }

  internal data object DataObject

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(DataObject).shouldBe("""{ }""")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<DataObject>("""{ }""").shouldBe(DataObject)
    }

  @Test
  fun `deserialize, unknown property`(): Unit =
    runTest {
      shouldThrowExactly<UnrecognizedPropertyException> {
        json.deserialize<DataObject>("""{"other":"unknown"}""")
      }.message.shouldStartWith(
        "Unrecognized field \"other\"",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DataObject>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kairo.serialization.DataObjectSerializationTest.DataObject(non-null)" +
          " but was null",
      )

      json.deserialize<DataObject?>("null").shouldBeNull()
    }
}

package kairo.serialization.format

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.LocalDate
import java.util.Optional
import kairo.serialization.jsonMapper
import kotlin.uuid.Uuid
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to the JSON data format.
 * Therefore, some test cases are not included since they are not strictly related to JSON.
 */
internal class JsonObjectMapperTest {
  internal data class MyClass(
    val booleans: Booleans,
    val float: Float,
    val floats: List<Float>,
    val int: Int,
    val strings: Strings,
    val uuid: Uuid,
    val nested: Nested,
    val optionals: Optionals,
    val instant: Instant,
    val localDate: LocalDate,
  ) {
    internal data class Booleans(
      val booleanTrue: Boolean,
      val booleanFalse: Boolean,
      val booleanNull: Boolean?,
    )

    internal data class Strings(
      val stringTrue: String,
      val stringFloat: String,
      val stringInt: String,
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    internal data class Optionals(
      val optionalPresent: Optional<Int>?,
      val optionalEmpty: Optional<Int>?,
      val optionalNull: Optional<Int>?,
    )

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
      JsonSubTypes.Type(Nested.NestA::class, name = "NestA"),
      JsonSubTypes.Type(Nested.NestB::class, name = "NestB"),
    )
    internal sealed class Nested {
      internal data class NestA(
        val a: String,
      ) : Nested()

      internal data class NestB(
        val b: String,
      ) : Nested()
    }
  }

  private val mapper: JsonMapper =
    jsonMapper {
      prettyPrint = true
    }

  private val myClass: MyClass =
    MyClass(
      booleans = MyClass.Booleans(
        booleanTrue = true,
        booleanFalse = false,
        booleanNull = null,
      ),
      float = 1.23F,
      floats = listOf(0F, 1.11F, 2.22F),
      int = 42,
      strings = MyClass.Strings(
        stringTrue = "true",
        stringFloat = "1.23",
        stringInt = "42",
      ),
      uuid = Uuid.parse("3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8"),
      nested = MyClass.Nested.NestB(b = "bravo"),
      optionals = MyClass.Optionals(
        optionalPresent = Optional.of(42),
        optionalEmpty = Optional.empty(),
        optionalNull = null,
      ),
      instant = Instant.parse("2023-11-13T19:44:32.123456789Z"),
      localDate = LocalDate.parse("2023-11-13"),
    )

  private val string: String = """
    {
      "booleans": {
        "booleanFalse": false,
        "booleanNull": null,
        "booleanTrue": true
      },
      "float": 1.23,
      "floats": [
        0.0,
        1.11,
        2.22
      ],
      "instant": "2023-11-13T19:44:32.123456789Z",
      "int": 42,
      "localDate": "2023-11-13",
      "nested": {
        "type": "NestB",
        "b": "bravo"
      },
      "optionals": {
        "optionalEmpty": null,
        "optionalPresent": 42
      },
      "strings": {
        "stringFloat": "1.23",
        "stringInt": "42",
        "stringTrue": "true"
      },
      "uuid": "3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8"
    }
  """.trimIndent()

  @Test
  fun serialize(): Unit = runTest {
    mapper.writeValueAsString(myClass).shouldBe(string)
  }

  @Test
  fun deserialize(): Unit = runTest {
    mapper.readValue<MyClass>(string).shouldBe(myClass)
  }
}

package kairo.serialization

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.LocalDate
import java.util.Optional
import java.util.UUID
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to the YAML data format.
 * Therefore, some test cases are not included since they are not strictly related to YAML.
 */
internal class YamlObjectMapperTest {
  internal data class MyClass(
    val booleans: Booleans,
    val float: Float,
    val int: Int,
    val strings: Strings,
    val uuid: UUID,
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
    ObjectMapperFactory.builder(ObjectMapperFormat.Yaml) {
      prettyPrint = true
    }.build()

  private val myClass: MyClass = MyClass(
    booleans = MyClass.Booleans(
      booleanTrue = true,
      booleanFalse = false,
      booleanNull = null,
    ),
    float = 1.23F,
    int = 42,
    strings = MyClass.Strings(
      stringTrue = "true",
      stringFloat = "1.23",
      stringInt = "42",
    ),
    uuid = UUID.fromString("3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8"),
    nested = MyClass.Nested.NestB(b = "bravo"),
    optionals = MyClass.Optionals(
      optionalPresent = Optional.of(42),
      optionalEmpty = Optional.empty(),
      optionalNull = null,
    ),
    instant = Instant.parse("2023-11-13T19:44:32.123456789Z"),
    localDate = LocalDate.parse("2023-11-13"),
  )

  val string = """
    booleans:
      booleanFalse: false
      booleanNull: null
      booleanTrue: true
    float: 1.23
    instant: "2023-11-13T19:44:32.123456789Z"
    int: 42
    localDate: "2023-11-13"
    nested:
      type: "NestB"
      b: "bravo"
    optionals:
      optionalEmpty: null
      optionalPresent: 42
    strings:
      stringFloat: "1.23"
      stringInt: "42"
      stringTrue: "true"
    uuid: "3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8"
  """.trimIndent() + '\n'

  @Test
  fun serialize() {
    mapper.writeValueAsString(myClass).shouldBe(string)
  }

  @Test
  fun deserialize() {
    mapper.readValue<MyClass>(string).shouldBe(myClass)
  }
}

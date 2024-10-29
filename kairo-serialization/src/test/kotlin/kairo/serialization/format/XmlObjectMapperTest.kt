package kairo.serialization.format

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.LocalDate
import java.util.Optional
import kairo.serialization.property.prettyPrint
import kairo.serialization.xmlMapper
import kotlin.uuid.Uuid
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to the XML data format.
 * Therefore, some test cases are not included since they are not strictly related to XML.
 */
internal class XmlObjectMapperTest {
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

  private val mapper: XmlMapper =
    xmlMapper {
      prettyPrint = true
    }.build()

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
    <?xml version='1.0' encoding='UTF-8'?>
    <MyClass>
      <booleans>
        <booleanFalse>false</booleanFalse>
        <booleanNull xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:nil="true"/>
        <booleanTrue>true</booleanTrue>
      </booleans>
      <float>1.23</float>
      <floats>
        <floats>0.0</floats>
        <floats>1.11</floats>
        <floats>2.22</floats>
      </floats>
      <instant>2023-11-13T19:44:32.123456789Z</instant>
      <int>42</int>
      <localDate>2023-11-13</localDate>
      <nested type="NestB">
        <b>bravo</b>
      </nested>
      <optionals>
        <optionalEmpty xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:nil="true"/>
        <optionalPresent>42</optionalPresent>
      </optionals>
      <strings>
        <stringFloat>1.23</stringFloat>
        <stringInt>42</stringInt>
        <stringTrue>true</stringTrue>
      </strings>
      <uuid>3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8</uuid>
    </MyClass>
  """.trimIndent() + '\n'

  @Test
  fun serialize(): Unit = runTest {
    mapper.writeValueAsString(myClass).shouldBe(string)
  }

  @Test
  fun deserialize(): Unit = runTest {
    mapper.readValue<MyClass>(string).shouldBe(myClass)
  }
}

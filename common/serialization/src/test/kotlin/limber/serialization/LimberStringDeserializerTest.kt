package limber.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class LimberStringDeserializerTest {
  private data class NoAnnotation(val string: String)
  private data class TrimNone(@TrimWhitespace(TrimWhitespace.Type.TrimNone) val string: String)
  private data class TrimStart(@TrimWhitespace(TrimWhitespace.Type.TrimStart) val string: String)
  private data class TrimEnd(@TrimWhitespace(TrimWhitespace.Type.TrimEnd) val string: String)
  private data class TrimBoth(@TrimWhitespace(TrimWhitespace.Type.TrimBoth) val string: String)

  @Test
  fun `default object mapper, no annotation`() {
    val objectMapper = objectMapper(withStringTrimModule = false)
    objectMapper.readValue<NoAnnotation>(json("value")).shouldBe(NoAnnotation("value"))
    objectMapper.readValue<NoAnnotation>(json(" value ")).shouldBe(NoAnnotation(" value "))
  }

  @Test
  fun `default object mapper, trim none annotation`() {
    val objectMapper = objectMapper(withStringTrimModule = false)
    objectMapper.readValue<TrimNone>(json("value")).shouldBe(TrimNone("value"))
    objectMapper.readValue<TrimNone>(json(" value ")).shouldBe(TrimNone(" value "))
  }

  @Test
  fun `default object mapper, trim start annotation`() {
    val objectMapper = objectMapper(withStringTrimModule = false)
    objectMapper.readValue<TrimStart>(json("value")).shouldBe(TrimStart("value"))
    objectMapper.readValue<TrimStart>(json(" value ")).shouldBe(TrimStart(" value "))
  }

  @Test
  fun `default object mapper, trim end annotation`() {
    val objectMapper = objectMapper(withStringTrimModule = false)
    objectMapper.readValue<TrimEnd>(json("value")).shouldBe(TrimEnd("value"))
    objectMapper.readValue<TrimEnd>(json(" value ")).shouldBe(TrimEnd(" value "))
  }

  @Test
  fun `default object mapper, trim both annotation`() {
    val objectMapper = objectMapper(withStringTrimModule = false)
    objectMapper.readValue<TrimBoth>(json("value")).shouldBe(TrimBoth("value"))
    objectMapper.readValue<TrimBoth>(json(" value ")).shouldBe(TrimBoth(" value "))
  }

  @Test
  fun `object mapper with string trim, no annotation`() {
    val objectMapper = objectMapper(withStringTrimModule = true)
    objectMapper.readValue<NoAnnotation>(json("value")).shouldBe(NoAnnotation("value"))
    objectMapper.readValue<NoAnnotation>(json(" value ")).shouldBe(NoAnnotation("value"))
  }

  @Test
  fun `object mapper with string trim, trim none annotation`() {
    val objectMapper = objectMapper(withStringTrimModule = true)
    objectMapper.readValue<TrimNone>(json("value")).shouldBe(TrimNone("value"))
    objectMapper.readValue<TrimNone>(json(" value ")).shouldBe(TrimNone(" value "))
  }

  @Test
  fun `object mapper with string trim, trim start annotation`() {
    val objectMapper = objectMapper(withStringTrimModule = true)
    objectMapper.readValue<TrimStart>(json("value")).shouldBe(TrimStart("value"))
    objectMapper.readValue<TrimStart>(json(" value ")).shouldBe(TrimStart("value "))
  }

  @Test
  fun `object mapper with string trim, trim end annotation`() {
    val objectMapper = objectMapper(withStringTrimModule = true)
    objectMapper.readValue<TrimEnd>(json("value")).shouldBe(TrimEnd("value"))
    objectMapper.readValue<TrimEnd>(json(" value ")).shouldBe(TrimEnd(" value"))
  }

  @Test
  fun `object mapper with string trim, trim both annotation`() {
    val objectMapper = objectMapper(withStringTrimModule = true)
    objectMapper.readValue<TrimBoth>(json("value")).shouldBe(TrimBoth("value"))
    objectMapper.readValue<TrimBoth>(json(" value ")).shouldBe(TrimBoth("value"))
  }

  private fun objectMapper(withStringTrimModule: Boolean): ObjectMapper =
    ObjectMapperFactory.builder(ObjectMapperFactory.Format.JSON).apply {
      if (withStringTrimModule) addModule(StringTrimModule)
    }.build()

  private fun json(value: String): String =
    "{\"string\":\"$value\"}"
}

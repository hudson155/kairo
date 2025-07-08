package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.util.kairoRead
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to string "trim whitespace" during deserialization.
 * Therefore, some test cases are not included since they are not strictly related to string "trim whitespace".
 */
internal class StringTrimWhitespaceObjectMapperTest {
  internal data class MyClass(
    val value: String,
  )

  data class MyClassTrimStart(
    @TrimWhitespace(TrimWhitespace.Type.TrimStart)
    val value: String,
  )

  data class MyClassTrimEnd(
    @TrimWhitespace(TrimWhitespace.Type.TrimEnd)
    val value: String,
  )

  data class MyClassTrimBoth(
    @TrimWhitespace(TrimWhitespace.Type.TrimBoth)
    val value: String,
  )

  private val string: String = "{ \"value\": \"  foo\\t\" }"

  @Test
  fun `deserialize, trim none`(): Unit = runTest {
    val mapper = createMapper()
    mapper.kairoRead<MyClass>(string).shouldBe(MyClass("  foo\t"))
  }

  @Test
  fun `deserialize, trim start`(): Unit = runTest {
    val mapper = createMapper(TrimWhitespace.Type.TrimStart)
    mapper.kairoRead<MyClass>(string).shouldBe(MyClass("foo\t"))
  }

  @Test
  fun `deserialize, trim end`(): Unit = runTest {
    val mapper = createMapper(TrimWhitespace.Type.TrimEnd)
    mapper.kairoRead<MyClass>(string).shouldBe(MyClass("  foo"))
  }

  @Test
  fun `deserialize, trim both`(): Unit = runTest {
    val mapper = createMapper(TrimWhitespace.Type.TrimBoth)
    mapper.kairoRead<MyClass>(string).shouldBe(MyClass("foo"))
  }

  @Test
  fun `deserialize, trim start, override`(): Unit = runTest {
    val mapper = createMapper(TrimWhitespace.Type.TrimNone)
    mapper.kairoRead<MyClassTrimStart>(string).shouldBe(MyClassTrimStart("foo\t"))
  }

  @Test
  fun `deserialize, trim end, override`(): Unit = runTest {
    val mapper = createMapper(TrimWhitespace.Type.TrimNone)
    mapper.kairoRead<MyClassTrimEnd>(string).shouldBe(MyClassTrimEnd("  foo"))
  }

  @Test
  fun `deserialize, trim both, override`(): Unit = runTest {
    val mapper = createMapper(TrimWhitespace.Type.TrimNone)
    mapper.kairoRead<MyClassTrimBoth>(string).shouldBe(MyClassTrimBoth("foo"))
  }

  private fun createMapper(trimWhitespace: TrimWhitespace.Type? = null): JsonMapper =
    jsonMapper {
      if (trimWhitespace != null) {
        this.trimWhitespace = trimWhitespace
      }
    }.build()
}

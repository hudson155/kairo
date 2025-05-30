package kairo.serialization.util

import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kairo.reflect.kairoType
import kairo.serialization.jsonMapper
import kairo.serialization.typeReference
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ReadValueSpecialTest {
  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `string, inline`(): Unit = runTest {
    mapper.readValueSpecial<String>("foo")
      .shouldBe("foo")
  }

  @Test
  fun `string, kairo type`(): Unit = runTest {
    mapper.readValueSpecial("foo", kairoType<String>())
      .shouldBe("foo")
  }

  @Test
  fun `string, type reference`(): Unit = runTest {
    mapper.readValueSpecial("foo", kairoType<String>().typeReference)
      .shouldBe("foo")
  }

  @Test
  fun `string, java type`(): Unit = runTest {
    mapper.readValueSpecial("foo", mapper.constructType(kairoType<String>().typeReference))
      .shouldBe("foo")
  }

  @Test
  fun `boolean, inline`(): Unit = runTest {
    mapper.readValueSpecial<Boolean>("true")
      .shouldBe(true)
  }

  @Test
  fun `boolean, kairo type`(): Unit = runTest {
    mapper.readValueSpecial("true", kairoType<Boolean>())
      .shouldBe(true)
  }

  @Test
  fun `boolean, type reference`(): Unit = runTest {
    mapper.readValueSpecial("true", kairoType<Boolean>().typeReference)
      .shouldBe(true)
  }

  @Test
  fun `boolean, java type`(): Unit = runTest {
    mapper.readValueSpecial("true", mapper.constructType(kairoType<Boolean>().typeReference))
      .shouldBe(true)
  }

  @Test
  fun `null, inline`(): Unit = runTest {
    mapper.readValueSpecial<Boolean>("null")
      .shouldBeNull()
  }

  @Test
  fun `null, kairo type`(): Unit = runTest {
    mapper.readValueSpecial("null", kairoType<Boolean>())
      .shouldBeNull()
  }

  @Test
  fun `null, type reference`(): Unit = runTest {
    mapper.readValueSpecial("null", kairoType<Boolean>().typeReference)
      .shouldBeNull()
  }

  @Test
  fun `null, java type`(): Unit = runTest {
    mapper.readValueSpecial("null", mapper.constructType(kairoType<Boolean>().typeReference))
      .shouldBeNull()
  }
}

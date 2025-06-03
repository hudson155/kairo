package kairo.serialization.util

import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.matchers.shouldBe
import kairo.reflect.kairoType
import kairo.serialization.jsonMapper
import kairo.serialization.typeReference
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KairoWriteSpecialTest {
  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `string, inline, non-null`(): Unit = runTest {
    mapper.kairoWriteSpecial<String>("foo")
      .shouldBe("foo")
  }

  @Test
  fun `string, kairo type`(): Unit = runTest {
    mapper.kairoWriteSpecial("foo", kairoType<String>())
      .shouldBe("foo")
  }

  @Test
  fun `string, type reference`(): Unit = runTest {
    mapper.kairoWriteSpecial("foo", kairoType<String>().typeReference)
      .shouldBe("foo")
  }

  @Test
  fun `string, java type`(): Unit = runTest {
    mapper.kairoWriteSpecial("foo", mapper.constructType(kairoType<String>().typeReference))
      .shouldBe("foo")
  }

  @Test
  fun `boolean, inline`(): Unit = runTest {
    mapper.kairoWriteSpecial<Boolean>(true)
      .shouldBe("true")
  }

  @Test
  fun `boolean, kairo type`(): Unit = runTest {
    mapper.kairoWriteSpecial(true, kairoType<Boolean>())
      .shouldBe("true")
  }

  @Test
  fun `boolean, type reference`(): Unit = runTest {
    mapper.kairoWriteSpecial(true, kairoType<Boolean>().typeReference)
      .shouldBe("true")
  }

  @Test
  fun `boolean, java type`(): Unit = runTest {
    mapper.kairoWriteSpecial(true, mapper.constructType(kairoType<Boolean>().typeReference))
      .shouldBe("true")
  }
}

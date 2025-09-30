package kairo.serialization

import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

internal class BigDecimalAsStringSerializerTest {
  @Serializable
  internal data class Wrapper(
    @Serializable(with = BigDecimalSerializer.AsString::class)
    val value: BigDecimal,
  )

  private val json: Json = json()

  @Test
  fun `serialize, 0`(): Unit =
    runTest {
      val value = BigDecimal("0")
      json.encodeToString(Wrapper(value))
        .shouldBe("""{"value":"0"}""")
    }

  @Test
  fun `serialize, maximum precision`(): Unit =
    runTest {
      val value = BigDecimal("0.8172843657189237")
      json.encodeToString(Wrapper(value))
        .shouldBe("""{"value":"0.8172843657189237"}""")
    }

  @Test
  fun `serialize, exceeds maximum precision`(): Unit =
    runTest {
      val value = BigDecimal("0.81728436571892375")
      json.encodeToString(Wrapper(value))
        .shouldBe("""{"value":"0.81728436571892375"}""")
    }
}

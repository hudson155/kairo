package kairo.serialization

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import java.math.BigInteger
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

internal class BigIntegerAsLongSerializerTest {
  @Serializable
  internal data class Wrapper(
    @Serializable(with = BigIntegerSerializer.AsLong::class)
    val value: BigInteger,
  )

  @Test
  fun `serialize, 0`(): Unit =
    runTest {
      val value = BigInteger("0")
      Json.encodeToString(Wrapper(value))
        .shouldBe("""{"value":0}""")
    }

  @Test
  fun `serialize, lowest long`(): Unit =
    runTest {
      val value = BigInteger("-9223372036854775808")
      Json.encodeToString(Wrapper(value))
        .shouldBe("""{"value":-9223372036854775808}""")
    }

  @Test
  fun `serialize, below lowest long`(): Unit =
    runTest {
      val value = BigInteger("-9223372036854775809")
      shouldThrow<ArithmeticException> {
        Json.encodeToString(Wrapper(value))
      }
    }

  @Test
  fun `serialize, highest long`(): Unit =
    runTest {
      val value = BigInteger("9223372036854775807")
      Json.encodeToString(Wrapper(value))
        .shouldBe("""{"value":9223372036854775807}""")
    }

  @Test
  fun `serialize, above highest long`(): Unit =
    runTest {
      val value = BigInteger("9223372036854775808")
      shouldThrow<ArithmeticException> {
        Json.encodeToString(Wrapper(value))
      }
    }
}

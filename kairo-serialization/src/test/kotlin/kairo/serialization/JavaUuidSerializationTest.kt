package kairo.serialization

import com.fasterxml.jackson.databind.exc.MismatchedInputException

import com.fasterxml.jackson.databind.exc.InvalidFormatException

import com.fasterxml.jackson.databind.RuntimeJsonMappingException

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.util.UUID
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class JavaUuidSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(UUID.fromString("a042df12-b775-42b2-aeb1-5bdd4ea78dc5"))
        .shouldBe("\"a042df12-b775-42b2-aeb1-5bdd4ea78dc5\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<UUID>("\"a042df12-b775-42b2-aeb1-5bdd4ea78dc5\"")
        .shouldBe(UUID.fromString("a042df12-b775-42b2-aeb1-5bdd4ea78dc5"))

      json.deserialize<UUID>("\"A042DF12-B775-42B2-AEB1-5BDD4EA78DC5\"")
        .shouldBe(UUID.fromString("a042df12-b775-42b2-aeb1-5bdd4ea78dc5"))
    }

  @Test
  fun `deserialize, wrong format (too short)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<UUID>("\"a042df12-b775-42b2-aeb1-5bdd4ea78dc\"")
      }
    }

  @Test
  fun `deserialize, wrong format (too long)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<UUID>("\"a042df12-b775-42b2-aeb1-5bdd4ea78dc5c\"")
      }
    }

  @Test
  fun `deserialize, wrong format (missing dashes)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<UUID>("\"a042df12b77542b2aeb15bdd4ea78dc5\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<UUID>("null")
      }

      json.deserialize<UUID?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<UUID>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<UUID>("2")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<UUID>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<UUID>("""[]""")
      }
    }
}

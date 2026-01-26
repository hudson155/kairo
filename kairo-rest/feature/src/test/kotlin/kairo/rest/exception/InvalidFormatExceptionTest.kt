package kairo.rest.exception

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.ktor.http.HttpStatusCode
import kairo.serialization.KairoJson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class InvalidFormatExceptionTest {
  internal data class Simple(
    val genre: Genre,
  ) {
    internal enum class Genre {
      Fantasy,
      History,
      Religion,
      Romance,
      Science,
      ScienceFiction,
    }
  }

  internal data class Nested(
    val simples: List<Simple>,
  )

  private val json: KairoJson = KairoJson()

  @Test
  fun simple(): Unit =
    runTest {
      val string = """{"genre":"Christianity"}"""
      val e = shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Simple>(string)
      }
      e.toLogicalFailure().shouldNotBeNull().json
        .shouldContainExactly(
          mapOf(
            "type" to "InvalidProperty",
            "status" to HttpStatusCode.BadRequest,
            "message" to "Invalid property",
            "detail" to null,
            "path" to "/genre",
          ),
        )
    }

  @Test
  fun nested(): Unit =
    runTest {
      val string = """{"simples":[{"genre":"Religion"},{"genre":"Christianity"}]}"""
      val e = shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Nested>(string)
      }
      e.toLogicalFailure().shouldNotBeNull().json
        .shouldContainExactly(
          mapOf(
            "type" to "InvalidProperty",
            "status" to HttpStatusCode.BadRequest,
            "message" to "Invalid property",
            "detail" to null,
            "path" to "/simples/1/genre",
          ),
        )
    }
}

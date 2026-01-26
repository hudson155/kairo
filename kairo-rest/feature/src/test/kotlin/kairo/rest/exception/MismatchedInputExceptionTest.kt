package kairo.rest.exception

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.ktor.http.HttpStatusCode
import kairo.serialization.KairoJson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class MismatchedInputExceptionTest {
  internal data class Simple(
    val foo: Int,
  )

  internal data class Nested(
    val simples: List<Simple>,
  )

  private val json: KairoJson = KairoJson()

  @Test
  fun simple(): Unit =
    runTest {
      val string = """{"foo":"bar"}"""
      val e = shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Simple>(string)
      }
      e.toLogicalFailure().shouldNotBeNull().json
        .shouldContainExactly(
          mapOf(
            "type" to "InvalidProperty",
            "status" to HttpStatusCode.BadRequest,
            "message" to "Invalid property",
            "detail" to null,
            "path" to "/foo",
          ),
        )
    }

  @Test
  fun nested(): Unit =
    runTest {
      val string = """{"simples":[{"foo":42},{"foo":"bar"}]}"""
      val e = shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Nested>(string)
      }
      e.toLogicalFailure().shouldNotBeNull().json
        .shouldContainExactly(
          mapOf(
            "type" to "InvalidProperty",
            "status" to HttpStatusCode.BadRequest,
            "message" to "Invalid property",
            "detail" to null,
            "path" to "/simples/1/foo",
          ),
        )
    }
}

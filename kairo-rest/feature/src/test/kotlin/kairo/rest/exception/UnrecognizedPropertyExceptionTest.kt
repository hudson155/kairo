package kairo.rest.exception

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.ktor.http.HttpStatusCode
import kairo.serialization.KairoJson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class UnrecognizedPropertyExceptionTest {
  internal data class Simple(
    val foo: String,
  )

  internal data class Nested(
    val simples: List<Simple>,
  )

  private val json: KairoJson = KairoJson()

  @Test
  fun simple(): Unit =
    runTest {
      val string = """{"foo":"bar","baz":"qux"}"""
      val e = shouldThrowExactly<UnrecognizedPropertyException> {
        json.deserialize<Simple>(string)
      }
      e.toLogicalFailure().shouldNotBeNull().json
        .shouldContainExactly(
          mapOf(
            "type" to "UnrecognizedProperty",
            "status" to HttpStatusCode.BadRequest,
            "message" to "Unrecognized property",
            "detail" to null,
            "path" to "/baz",
          ),
        )
    }

  @Test
  fun nested(): Unit =
    runTest {
      val string = """{"simples":[{"foo":"bar"},{"foo":"bar","baz":"qux"}]}"""
      val e = shouldThrowExactly<UnrecognizedPropertyException> {
        json.deserialize<Nested>(string)
      }
      e.toLogicalFailure().shouldNotBeNull().json
        .shouldContainExactly(
          mapOf(
            "type" to "UnrecognizedProperty",
            "status" to HttpStatusCode.BadRequest,
            "message" to "Unrecognized property",
            "detail" to null,
            "path" to "/simples/1/baz",
          ),
        )
    }
}

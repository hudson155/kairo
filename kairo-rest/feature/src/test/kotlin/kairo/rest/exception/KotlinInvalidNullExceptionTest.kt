package kairo.rest.exception

import com.fasterxml.jackson.module.kotlin.KotlinInvalidNullException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.ktor.http.HttpStatusCode
import kairo.serialization.KairoJson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KotlinInvalidNullExceptionTest {
  internal data class Simple(
    val foo: String,
  )

  internal data class Nested(
    val simples: List<Simple>,
  )

  private val json: KairoJson = KairoJson()

  @Test
  fun `simple, missing`(): Unit =
    runTest {
      val string = """{}"""
      val e = shouldThrowExactly<KotlinInvalidNullException> {
        json.deserialize<Simple>(string)
      }
      e.toLogicalFailure().shouldNotBeNull().json
        .shouldContainExactly(
          mapOf(
            "type" to "MissingProperty",
            "status" to HttpStatusCode.BadRequest,
            "message" to "Missing property",
            "detail" to null,
            "path" to "/foo",
          ),
        )
    }

  @Test
  fun `simple, null`(): Unit =
    runTest {
      val string = """{"foo":null}"""
      val e = shouldThrowExactly<KotlinInvalidNullException> {
        json.deserialize<Simple>(string)
      }
      e.toLogicalFailure().shouldNotBeNull().json
        .shouldContainExactly(
          mapOf(
            "type" to "MissingProperty",
            "status" to HttpStatusCode.BadRequest,
            "message" to "Missing property",
            "detail" to null,
            "path" to "/foo",
          ),
        )
    }

  @Test
  fun `nested, missing`(): Unit =
    runTest {
      val string = """{"simples":[{"foo":"bar"},{}]}"""
      val e = shouldThrowExactly<KotlinInvalidNullException> {
        json.deserialize<Nested>(string)
      }
      e.toLogicalFailure().shouldNotBeNull().json
        .shouldContainExactly(
          mapOf(
            "type" to "MissingProperty",
            "status" to HttpStatusCode.BadRequest,
            "message" to "Missing property",
            "detail" to null,
            "path" to "/simples/1/foo",
          ),
        )
    }

  @Test
  fun `nested, null`(): Unit =
    runTest {
      val string = """{"simples":[{"foo":"bar"},{"foo":null}]}"""
      val e = shouldThrowExactly<KotlinInvalidNullException> {
        json.deserialize<Nested>(string)
      }
      e.toLogicalFailure().shouldNotBeNull().json
        .shouldContainExactly(
          mapOf(
            "type" to "MissingProperty",
            "status" to HttpStatusCode.BadRequest,
            "message" to "Missing property",
            "detail" to null,
            "path" to "/simples/1/foo",
          ),
        )
    }
}

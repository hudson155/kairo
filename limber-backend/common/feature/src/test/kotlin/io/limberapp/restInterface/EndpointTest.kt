package io.limberapp.restInterface

import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.limberapp.rep.CreationRep
import io.limberapp.validation.RepValidation
import io.limberapp.validation.ifPresent
import org.junit.jupiter.api.Test
import java.lang.reflect.InvocationTargetException
import java.time.ZoneId
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class EndpointTest {
  /**
   * This singleton is declared as a field because singletons can't be locals. Local classes should
   * be preferred everywhere they are allowed in this test class.
   */
  internal object SingletonEndpoint : Endpoint(HttpMethod.Get, "/foo/bar/baz")

  internal enum class Enum1 { OPT_A, OPT_B }

  internal enum class Enum2 { ONE }

  internal data class TestRep(
      val prop1: String,
      val prop2: Int?,
      val prop3: Boolean,
  ) : CreationRep {
    override fun validate(): RepValidation = RepValidation {
      validate(TestRep::prop1) { length >= 3 }
      validate(TestRep::prop2) { ifPresent { this > 0 } }
      validate(TestRep::prop3) { !this }
    }
  }

  @Test
  fun `happy path - simple singleton object`() {
    assertEquals(EndpointTemplate(
        httpMethod = HttpMethod.Get,
        pathTemplate = "/foo/bar/baz",
        requiredQueryParams = emptySet(),
        contentType = ContentType.Application.Json,
    ), SingletonEndpoint::class.template())
  }

  @Test
  fun `happy path - simple class`() {
    data class TestEndpoint(val bar: String) : Endpoint(HttpMethod.Get, "/foo/$bar/baz")
    assertEquals(EndpointTemplate(
        httpMethod = HttpMethod.Get,
        pathTemplate = "/foo/{bar}/baz",
        requiredQueryParams = emptySet(),
        contentType = ContentType.Application.Json,
    ), TestEndpoint::class.template())
  }

  @Test
  fun `happy path - complex class`() {
    data class TestEndpoint(
        val varA: Int, val varB: String, val varC: ZoneId, val varD: UUID, val varE: Enum1,
        val varF: Int, val varG: String?, val varH: ZoneId?, val varI: UUID, val varJ: Enum2?,
        val varK: TestRep?,
    ) : Endpoint(
        httpMethod = HttpMethod.Post,
        // The path is inconsistent on purpose.
        rawPath = "/$varA/b/$varB/c/$varC/$varD/e/$varE",
        qp = listOfNotNull(
            "qpf" to varF.toString(),
            varG?.let { "qpg" to it },
            varH?.let { "qph" to it.toString() },
            "qpi" to varI.toString(),
            varJ?.let { "qph" to it.toString() },
        ),
        contentType = ContentType.Text.CSV,
        body = varK,
    )
    assertEquals(EndpointTemplate(
        httpMethod = HttpMethod.Post,
        pathTemplate = "/{varA}/b/{varB}/c/{varC}/{varD}/e/{varE}",
        requiredQueryParams = setOf("varF", "varI"),
        contentType = ContentType.Text.CSV,
    ), TestEndpoint::class.template())
  }

  @Test
  fun `negative - var is used twice`() {
    data class TestEndpoint(val bar: String) : Endpoint(HttpMethod.Get, "/foo/$bar/$bar")
    assertFails { TestEndpoint::class.template() }.let {
      val message = assertNotNull(it.message)
      assertTrue(message.contains(" contains more than 1 match of "), message = message)
    }
  }

  @Test
  fun `negative - rep is not nullable`() {
    data class TestEndpoint(val rep: TestRep) : Endpoint(HttpMethod.Get, "/foo/bar/baz", body = rep)
    assertFailsWith<InvocationTargetException> { TestEndpoint::class.template() }.let { e ->
      val cause = assertNotNull(e.cause)
      assertTrue(cause is NullPointerException)
      val message = assertNotNull(cause.message)
      assertTrue(message.startsWith("Parameter specified as non-null is null: "), message = message)
    }
  }
}

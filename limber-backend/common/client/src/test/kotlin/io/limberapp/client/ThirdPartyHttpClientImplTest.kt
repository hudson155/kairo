package io.limberapp.client

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.api.typicodePost.TypicodePostApi
import io.limberapp.client.exception.LimberHttpClientException
import io.limberapp.rep.typicodePost.TypicodePostFixtures
import io.limberapp.rep.typicodePost.TypicodePostRep
import io.limberapp.serialization.LimberObjectMapper
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Note: This test class interacts with a 3rd-party API. Be careful not to send any sensitive data.
 * Additionally, this test may fail if the API is down or changes.
 */
internal class ThirdPartyHttpClientImplTest {
  private val objectMapper: LimberObjectMapper = LimberObjectMapper()
  private val httpClient: HttpClient =
      HttpClientImpl("https://jsonplaceholder.typicode.com", objectMapper)

  @Test
  fun `Get - found`(): Unit = runBlocking {
    val result: TypicodePostRep.Complete? =
        httpClient.request(TypicodePostApi.Get(postId = 2), null) {
          it?.let { readValue(it) }
        }
    assertEquals(TypicodePostFixtures[2], result)
  }

  @Test
  fun `Get - not found`(): Unit = runBlocking {
    val result: TypicodePostRep.Complete? =
        httpClient.request(TypicodePostApi.Get(postId = 0), null) {
          it?.let { readValue(it) }
        }
    assertNull(result)
  }

  @Test
  fun `GetByUserId - found`(): Unit = runBlocking {
    val result: Set<TypicodePostRep.Complete> =
        httpClient.request(TypicodePostApi.GetByUserId(userId = 1), null) {
          readValue(checkNotNull(it))
        }
    assertEquals(TypicodePostFixtures.values.filter { it.userId == 1 }.toSet(), result)
  }

  @Test
  fun `Post - valid`(): Unit = runBlocking {
    val result: TypicodePostRep.Complete? = httpClient.request(TypicodePostApi.Post(
        rep = TypicodePostRep.Creation(title = "foo", body = "bar", userId = 1),
    ), null) {
      it?.let { readValue(it) }
    }
    val expected: TypicodePostRep.Complete = TypicodePostRep.Complete(
        userId = 1,
        id = 101,
        title = "foo",
        body = "bar",
    )
    assertEquals(expected, result)
  }

  @Test
  fun `Post - malformed JSON`(): Unit = runBlocking {
    /**
     * This pretty printer intentionally malforms the JSON by doubling the colon that separates the
     * key from the value.
     */
    class BrokenPrettyPrinter : DefaultPrettyPrinter() {
      override fun createInstance(): BrokenPrettyPrinter = BrokenPrettyPrinter()
      override fun writeObjectFieldValueSeparator(jg: JsonGenerator) {
        jg.writeRaw(_separators.objectFieldValueSeparator + ": ")
      }
    }

    val objectMapper = LimberObjectMapper(prettyPrint = true).apply {
      setDefaultPrettyPrinter(BrokenPrettyPrinter())
    }
    val httpClient: HttpClient =
        HttpClientImpl("https://jsonplaceholder.typicode.com", objectMapper)

    assertFailsWith<LimberHttpClientException> {
      httpClient.request(TypicodePostApi.Post(TypicodePostRep.Creation(
          title = "foo",
          body = "bar",
          userId = 1,
      )), null) {
        it?.let { readValue(it) }
      }
    }.let { e ->
      assertTrue(e.errorMessage.contains("SyntaxError: Unexpected token : in JSON"))
    }
  }
}

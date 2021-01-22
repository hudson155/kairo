package io.limberapp.common.client

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class HttpClientRequestBuilderTest {
  @Test
  fun default() {
    val requestBuilder = LimberHttpClientRequestBuilder(ContentType.Application.Json)
    assertEquals(mapOf("Accept" to "application/json"), requestBuilder.headers)
  }

  @Test
  fun `different accept header passed in`() {
    val requestBuilder = LimberHttpClientRequestBuilder(ContentType.Text.CSV)
    assertEquals(mapOf("Accept" to "text/csv"), requestBuilder.headers)
  }

  @Test
  fun `additional headers by builder`() {
    val requestBuilder = LimberHttpClientRequestBuilder(ContentType.Application.Json)
    requestBuilder.putHeader(HttpHeaders.Age, "12345")
    requestBuilder.putHeader(HttpHeaders.If, "condition")
    assertEquals(mapOf(
        "Accept" to "application/json",
        "Age" to "12345",
        "If" to "condition",
    ), requestBuilder.headers)
  }

  @Test
  fun `overriding header by builder`() {
    val requestBuilder = LimberHttpClientRequestBuilder(ContentType.Application.Json)
    requestBuilder.putHeader(HttpHeaders.Age, "12345")
    requestBuilder.putHeader(HttpHeaders.Age, "23456")
    requestBuilder.putHeader(HttpHeaders.Accept, "text/plain")
    assertFails {
      requestBuilder.putHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }
    assertEquals(mapOf(
        "Accept" to "text/plain",
        "Age" to "23456",
    ), requestBuilder.headers)
  }
}

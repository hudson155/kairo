package io.limberapp.common.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.HttpStatusCode
import io.ktor.util.KtorExperimentalAPI
import io.limberapp.common.restInterface.LimberEndpoint

open class LimberHttpClientImpl(private val baseUrl: String) : LimberHttpClient() {
  @OptIn(KtorExperimentalAPI::class)
  private val httpClient = HttpClient(CIO)

  final override suspend fun request(
      endpoint: LimberEndpoint,
      builder: LimberHttpClientRequestBuilder.() -> Unit,
  ): Pair<HttpStatusCode, String> {
    val httpResponse = httpClient.request<HttpResponse> {
      method = endpoint.httpMethod
      url(baseUrl + endpoint.href)
      val requestBuilder = LimberHttpClientRequestBuilder(accept = endpoint.contentType).apply {
        rootBuilder()
        builder()
      }
      requestBuilder.headers.forEach { (key, value) -> header(key, value) }
      endpoint.body?.let { body = it }
    }
    return Pair(httpResponse.status, httpResponse.readText())
  }

  protected open fun LimberHttpClientRequestBuilder.rootBuilder() {}

  final override fun close() = httpClient.close()
}

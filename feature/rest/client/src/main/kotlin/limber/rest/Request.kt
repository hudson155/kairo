package limber.rest

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.accept
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

@Suppress("SuspendFunWithCoroutineScopeReceiver")
public suspend inline fun <reified T> HttpClient.request(endpoint: RestEndpoint): T {
  val httpResponse = request {
    expectSuccess = true
    method = endpoint.method
    url(endpoint.path)
    accept(ContentType.Application.Json)
    endpoint.body?.let { body ->
      contentType(ContentType.Application.Json)
      setBody(body)
    }
  }

  if (httpResponse.status == HttpStatusCode.NotFound) {
    return null as T // Will NPE if T is not nullable.
  }
  return httpResponse.body()
}

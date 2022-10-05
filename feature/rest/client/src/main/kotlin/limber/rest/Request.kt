@file:Suppress("SuspendFunWithCoroutineScopeReceiver")

package limber.rest

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DataConversion
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.plugins.plugin
import io.ktor.client.request.accept
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo

public suspend inline fun <reified T> HttpClient.request(endpoint: RestEndpoint): T =
  request(endpoint, typeInfo<T>())

public suspend fun <T> HttpClient.request(endpoint: RestEndpoint, typeInfo: TypeInfo): T {
  val httpResponse = try {
    request {
      expectSuccess = true
      method = endpoint.method
      url(href(endpoint))
      accept(ContentType.Application.Json)
      endpoint.body?.let { body ->
        contentType(ContentType.Application.Json)
        setBody(body)
      }
    }
  } catch (e: ClientRequestException) {
    if (e.response.status == HttpStatusCode.NotFound) {
      @Suppress("UNCHECKED_CAST")
      return null as T // Will NPE if T is not nullable.
    }
    throw e
  }
  return httpResponse.body(typeInfo)
}

private fun HttpClient.href(endpoint: RestEndpoint): String {
  val conversionService = plugin(DataConversion)

  var result = endpoint.path
  if (endpoint.qp.isNotEmpty()) {
    val parameters = Parameters.build {
      endpoint.qp.forEach { qp ->
        appendAll(qp.name, conversionService.toValues(qp.get()))
      }
    }
    result += "?${parameters.formUrlEncode()}"
  }
  return result
}

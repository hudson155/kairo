package kairo.rest.exceptionHandler

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

@Suppress("SuspendFunWithCoroutineScopeReceiver")
internal suspend fun ApplicationCall.respondWithError(error: Pair<HttpStatusCode, Any?>) {
  val (statusCode, response) = error
  if (response != null) {
    respond(statusCode, response)
  } else {
    respond(statusCode)
  }
}

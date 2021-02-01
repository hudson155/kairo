package io.limberapp.common.server

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.limberapp.common.auth.Auth
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.common.restInterface.template

internal class UnusualStatusCodeHandler : EndpointHandler<TestApi.UnusualStatusCode, Unit>(
    template = TestApi.UnusualStatusCode::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TestApi.UnusualStatusCode =
      TestApi.UnusualStatusCode

  override suspend fun Handler.handle(endpoint: TestApi.UnusualStatusCode) {
    auth { Auth.Allow }
    statusCode = HttpStatusCode.Accepted
  }
}

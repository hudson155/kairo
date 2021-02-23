package io.limberapp.server

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template

internal class EndpointWithoutAuthHandler @Inject constructor(
) : EndpointHandler<TestApi.EndpointWithoutAuth, Unit>(
    template = TestApi.EndpointWithoutAuth::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TestApi.EndpointWithoutAuth =
      TestApi.EndpointWithoutAuth

  override suspend fun Handler.handle(endpoint: TestApi.EndpointWithoutAuth): Unit = Unit
}
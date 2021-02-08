package io.limberapp.common.server

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.common.restInterface.template

internal class EndpointWithoutAuthHandler @Inject constructor(
) : EndpointHandler<TestApi.EndpointWithoutAuth, Unit>(
    template = TestApi.EndpointWithoutAuth::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TestApi.EndpointWithoutAuth =
      TestApi.EndpointWithoutAuth

  override suspend fun Handler.handle(endpoint: TestApi.EndpointWithoutAuth): Unit = Unit
}

package io.limberapp.backend.endpoint.test

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.backend.api.test.TestApi
import io.limberapp.backend.service.test.TestService
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.common.restInterface.template

internal class SingletonEndpointHandler @Inject constructor(
    @Suppress("UNUSED_PARAMETER") testService: TestService,
) : EndpointHandler<TestApi.Singleton, Unit>(
    template = TestApi.Singleton::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TestApi.Singleton = TestApi.Singleton
  override suspend fun Handler.handle(endpoint: TestApi.Singleton): Unit = Unit
}

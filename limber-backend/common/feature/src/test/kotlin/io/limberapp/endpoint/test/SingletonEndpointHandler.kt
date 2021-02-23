package io.limberapp.endpoint.test

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.test.TestApi
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.test.TestService

internal class SingletonEndpointHandler @Inject constructor(
    @Suppress("UNUSED_PARAMETER") testService: TestService,
) : EndpointHandler<TestApi.Singleton, Unit>(
    template = TestApi.Singleton::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TestApi.Singleton = TestApi.Singleton
  override suspend fun Handler.handle(endpoint: TestApi.Singleton): Unit = Unit
}

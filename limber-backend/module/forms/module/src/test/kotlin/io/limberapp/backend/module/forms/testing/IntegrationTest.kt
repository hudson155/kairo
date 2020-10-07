package io.limberapp.backend.module.forms.testing

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.forms.client.formTemplate.FormTemplateClient
import io.limberapp.common.LimberApplication
import io.limberapp.testing.integration.LimberIntegrationTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(IntegrationTestExtension::class)
internal abstract class IntegrationTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : LimberIntegrationTest(engine, limberServer) {
  protected val formTemplateClient by lazy { FormTemplateClient(httpClient) }
}

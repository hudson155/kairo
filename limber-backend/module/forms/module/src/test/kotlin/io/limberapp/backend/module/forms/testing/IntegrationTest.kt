package io.limberapp.backend.module.forms.testing

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.forms.client.formInstance.FormInstanceClient
import io.limberapp.backend.module.forms.client.formTemplate.FormTemplateClient
import io.limberapp.backend.module.forms.client.formTemplate.FormTemplateQuestionClient
import io.limberapp.common.LimberApplication
import io.limberapp.testing.integration.LimberIntegrationTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(IntegrationTestExtension::class)
internal abstract class IntegrationTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : LimberIntegrationTest(engine, limberServer) {
  protected val formInstanceClient by lazy { FormInstanceClient(httpClient) }
  protected val formTemplateClient by lazy { FormTemplateClient(httpClient) }
  protected val formTemplateQuestionClient by lazy { FormTemplateQuestionClient(httpClient) }
}

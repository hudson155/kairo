package io.limberapp.module.graphql.testing

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(IntegrationTestExtension::class)
internal abstract class IntegrationTest(engine: TestApplicationEngine) : IntegrationTest(engine)

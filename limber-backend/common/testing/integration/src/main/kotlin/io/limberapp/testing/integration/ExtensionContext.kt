package io.limberapp.testing.integration

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.extension.ExtensionContext

internal val TEST_CONTEXT = mapOf(
    LimberApplication::class.java to "LIMBER_SERVER",
    TestApplicationEngine::class.java to "TEST_APPLICATION_ENGINE",
    LimberIntegrationTestExtension::class.java to "INTEGRATION_TEST_EXTENSION",
)

@Suppress("UNCHECKED_CAST")
internal operator fun <T> ExtensionContext.get(key: Class<T>) =
    with(root.getStore(ExtensionContext.Namespace.GLOBAL)) { get(TEST_CONTEXT[key]) as T }

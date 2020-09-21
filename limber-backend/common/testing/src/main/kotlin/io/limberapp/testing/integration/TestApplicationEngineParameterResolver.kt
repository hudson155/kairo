package io.limberapp.testing.integration

import io.ktor.server.testing.TestApplicationEngine
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

internal class TestApplicationEngineParameterResolver : ParameterResolver {
  override fun supportsParameter(
    parameterContext: ParameterContext,
    extensionContext: ExtensionContext,
  ) = parameterContext.parameter.type === TestApplicationEngine::class.java

  override fun resolveParameter(
    parameterContext: ParameterContext,
    extensionContext: ExtensionContext,
  ): TestApplicationEngine {
    return with(extensionContext.root.getStore(ExtensionContext.Namespace.GLOBAL)) {
      get("TEST_APPLICATION_ENGINE") as TestApplicationEngine
    }
  }
}

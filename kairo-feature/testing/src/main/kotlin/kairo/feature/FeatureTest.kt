package kairo.feature

import kairo.server.Server
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication

public abstract class FeatureTest : FeatureTestAware, BeforeEachCallback, AfterEachCallback, ParameterResolver {
  override fun beforeEach(context: ExtensionContext) {
    val koinApplication = koinApplication()
    context.koin = koinApplication.koin
    val server = createServer(context, koinApplication)
    context.server = server
    runBlocking {
      server.start()
    }
  }

  public abstract fun createServer(context: ExtensionContext, koinApplication: KoinApplication): Server

  override fun afterEach(context: ExtensionContext) {
    context.server?.let { server ->
      runBlocking {
        server.stop()
      }
    }
  }

  override fun supportsParameter(
    parameterContext: ParameterContext,
    extensionContext: ExtensionContext,
  ): Boolean {
    val kClass = parameterContext.parameter.type.kotlin
    when (kClass) {
      Koin::class -> return true
      Server::class -> return true
    }
    val koin = checkNotNull(extensionContext.koin)
    @Suppress("UndeclaredKoinUsage")
    return koin.getOrNull<Any>(kClass) != null
  }

  override fun resolveParameter(
    parameterContext: ParameterContext,
    extensionContext: ExtensionContext,
  ): Any {
    val kClass = parameterContext.parameter.type.kotlin
    when (kClass) {
      Koin::class -> return checkNotNull(extensionContext.koin)
      Server::class -> return checkNotNull(extensionContext.server)
    }
    val koin = checkNotNull(extensionContext.koin)
    @Suppress("UndeclaredKoinUsage")
    return koin.get(kClass)
  }

  public companion object {
    public val namespace: ExtensionContext.Namespace =
      ExtensionContext.Namespace.create(FeatureTest::class)
  }
}

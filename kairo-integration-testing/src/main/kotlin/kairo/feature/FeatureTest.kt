package kairo.feature

import kairo.dependencyInjection.KoinExtension
import kairo.dependencyInjection.koin
import kairo.server.Server
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.KoinApplication

public abstract class FeatureTest : KoinExtension(), FeatureTestAware, AfterEachCallback {
  override fun beforeEach(context: ExtensionContext) {
    super.beforeEach(context)
    val server = createServer(context, checkNotNull(context.koin))
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

  public companion object {
    public val namespace: ExtensionContext.Namespace =
      ExtensionContext.Namespace.create(FeatureTest::class)
  }
}

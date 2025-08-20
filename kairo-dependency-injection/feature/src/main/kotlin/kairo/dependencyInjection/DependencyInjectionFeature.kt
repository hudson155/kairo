package kairo.dependencyInjection

import kairo.feature.Feature
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * The Dependency Injection Feature enables Koin for dependency injection,
 * running the Koin application for the lifecycle of the Server.
 */
public class DependencyInjectionFeature(
  private val block: KoinAppDeclaration,
) : Feature() {
  override val name: String = "Dependency Injection"

  override suspend fun start(features: List<Feature>) {
    startKoin(block)
  }

  override suspend fun stop() {
    stopKoin()
  }
}

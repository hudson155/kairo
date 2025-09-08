package kairo.dependencyInjection

import kairo.feature.Feature
import org.koin.core.KoinApplication

/**
 * The Dependency Injection Feature enables Koin for dependency injection,
 * allowing other Features to bind Koin dependencies by implementing [KoinModule].
 */
public class DependencyInjectionFeature(
  private val application: KoinApplication,
) : Feature() {
  override val name: String = "Dependency Injection"

  override fun beforeStart(features: List<Feature>) {
    features.forEach { feature ->
      if (feature !is KoinModule) return@forEach
      application.modules(feature.koinModule)
    }
  }

  public companion object
}

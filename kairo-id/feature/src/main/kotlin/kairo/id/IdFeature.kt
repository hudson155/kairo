package kairo.id

import kairo.dependencyInjection.DependencyInjectionFeature
import kairo.feature.Feature
import org.koin.core.KoinApplication
import org.koin.dsl.module

public class IdFeature(
  private val config: IdFeatureConfig = IdFeatureConfig(),
) : Feature(), DependencyInjectionFeature.HasBindings {
  override val name: String = "ID"

  override fun KoinApplication.binding() {
    modules(
      module {
        single<IdGenerationStrategy> { generationStrategy() }
      },
    )
  }

  private fun generationStrategy(): RandomIdGenerationStrategy =
    when (config.generation) {
      is IdFeatureConfig.Generation.Random ->
        RandomIdGenerationStrategy(
          length = config.generation.length,
        )
    }
}

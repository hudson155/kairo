package kairo.id

import kairo.dependencyInjection.HasBindings
import kairo.feature.Feature
import org.koin.core.KoinApplication
import org.koin.dsl.module

/**
 * Binds Kairo ID generation for use within a Kairo application.
 */
public class IdFeature(
  private val config: IdFeatureConfig = IdFeatureConfig(),
) : Feature(), HasBindings {
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

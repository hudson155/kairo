package kairo.id

import kairo.dependencyInjection.KoinModule
import kairo.feature.Feature
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Binds Kairo ID generation for use within a Kairo application.
 */
public class IdFeature(
  private val config: IdFeatureConfig = IdFeatureConfig(),
) : Feature(), KoinModule {
  override val name: String = "ID"

  override val koinModule: Module =
    module {
      single<IdGenerationStrategy> { generationStrategy() }
    }

  private fun generationStrategy(): RandomIdGenerationStrategy =
    when (config.generation) {
      is IdFeatureConfig.Generation.Random ->
        RandomIdGenerationStrategy(
          length = config.generation.length,
        )
    }
}

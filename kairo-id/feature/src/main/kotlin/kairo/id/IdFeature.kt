package kairo.id

import com.typesafe.config.ConfigFactory
import kairo.dependencyInjection.KoinModule
import kairo.feature.Feature
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Binds Kairo ID generation for use within a Kairo application.
 */
public class IdFeature : Feature(), KoinModule {
  override val name: String = "ID"

  private val config: IdFeatureConfig =
    ConfigFactory.load().getConfig("kairo.id")
      .let { Hocon.decodeFromConfig(it) }

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

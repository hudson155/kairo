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

  override val koinModule: Module =
    module {
      single<IdGenerationStrategy> { generationStrategy() }
    }

  private fun generationStrategy(): RandomIdGenerationStrategy {
    val idGeneration = ConfigFactory.load().getConfig("kairo.id.generation")
      .let { Hocon.decodeFromConfig<IdGeneration>(it) }
    return when (idGeneration) {
      is IdGeneration.Random ->
        RandomIdGenerationStrategy(
          length = idGeneration.length,
        )
    }
  }
}

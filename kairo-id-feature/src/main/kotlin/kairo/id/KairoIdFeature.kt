package kairo.id

import com.google.inject.Binder
import kairo.dependencyInjection.bind
import kairo.feature.Feature
import kairo.feature.FeaturePriority

public open class KairoIdFeature(
  private val config: KairoIdConfig,
) : Feature() {
  final override val name: String = "Kairo ID"

  final override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: Binder) {
    binder.bind<KairoIdGenerator.Factory>().toInstance(createGenerator(config.generator))
  }

  private fun createGenerator(config: KairoIdConfig.Generator): KairoIdGenerator.Factory =
    when (config) {
      is KairoIdConfig.Generator.Deterministic -> DeterministicKairoIdGenerator.Factory
      is KairoIdConfig.Generator.Random -> RandomKairoIdGenerator.Factory(length = config.length)
    }
}

package kairo.uuid

import com.google.inject.Binder
import kairo.dependencyInjection.bind
import kairo.feature.Feature
import kairo.feature.FeaturePriority

public open class KairoUuidFeature(
  private val config: KairoUuidConfig,
) : Feature() {
  final override val name: String = "Kairo UUID"

  final override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: Binder) {
    binder.bind<KairoUuidGenerator>().toInstance(createGenerator(config.generator))
  }

  private fun createGenerator(config: KairoUuidConfig.Generator): KairoUuidGenerator =
    when (config) {
      is KairoUuidConfig.Generator.Deterministic -> DeterministicKairoUuidGenerator()
      is KairoUuidConfig.Generator.Random -> RandomKairoUuidGenerator()
    }
}

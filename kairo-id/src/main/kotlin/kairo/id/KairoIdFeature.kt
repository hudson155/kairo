package kairo.id

import com.google.inject.PrivateBinder
import kairo.dependencyInjection.bind
import kairo.feature.Feature
import kairo.feature.FeaturePriority

public class KairoIdFeature(
  private val config: KairoIdConfig,
) : Feature() {
  override val name: String = "KairoId"

  override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: PrivateBinder) {
    binder.bind<KairoIdGenerator.Factory>().toInstance(createGenerator(config.generator))
  }

  private fun createGenerator(config: KairoIdConfig.Generator): KairoIdGenerator.Factory =
    when (config) {
      is KairoIdConfig.Generator.Deterministic -> DeterministicKairoIdGenerator.Factory(length = config.length)
      is KairoIdConfig.Generator.Random -> RandomKairoIdGenerator.Factory(length = config.length)
    }
}

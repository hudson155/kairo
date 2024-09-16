package kairo.idTesting

import com.google.inject.Injector
import kairo.dependencyInjection.getInstance
import kairo.featureTesting.TestFeature
import kairo.id.DeterministicKairoIdGenerator
import kairo.id.KairoIdConfig
import kairo.id.KairoIdFeature
import kairo.id.KairoIdGenerator

/**
 * Extends [KairoIdFeature] for testing by automatically resetting.
 */
public open class TestKairoIdFeature(
  config: KairoIdConfig,
) : KairoIdFeature(config), TestFeature.BeforeEach {
  override suspend fun beforeEachTest(injector: Injector) {
    val idGenerator = injector.getInstance<KairoIdGenerator>() as DeterministicKairoIdGenerator
    idGenerator.reset()
  }
}

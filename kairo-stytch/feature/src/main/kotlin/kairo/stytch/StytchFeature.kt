package kairo.stytch

import com.stytch.java.consumer.StytchClient
import kairo.dependencyInjection.HasKoinModules
import kairo.feature.Feature
import kairo.protectedString.ProtectedString
import org.koin.core.module.Module
import org.koin.dsl.module

public class StytchFeature(
  config: StytchFeatureConfig,
) : Feature(), HasKoinModules {
  override val name: String = "Stytch"

  private val stytchClient: StytchClient by lazy {
    StytchClient(
      config.projectId,
      @OptIn(ProtectedString.Access::class)
      config.secret.value,
    )
  }

  override val koinModules: List<Module> =
    listOf(
      module {
        single { stytchClient }
      },
    )
}

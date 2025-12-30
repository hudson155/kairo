package kairo.stytch

import com.stytch.java.consumer.StytchClient
import kairo.dependencyInjection.HasKoinModules
import kairo.feature.Feature
import kairo.protectedString.ProtectedString
import org.koin.core.module.Module
import org.koin.dsl.module

public class StytchFeature(
  stytch: Lazy<Stytch>,
) : Feature(), HasKoinModules {
  override val name: String = "Stytch"

  override val koinModules: List<Module> =
    listOf(
      module {
        single { stytch.value }
      },
    )

  public constructor(config: StytchFeatureConfig) : this(
    lazy {
      val stytchClient = StytchClient(
        config.projectId,
        @OptIn(ProtectedString.Access::class)
        config.secret.value,
      )
      return@lazy Stytch(stytchClient)
    },
  )
}

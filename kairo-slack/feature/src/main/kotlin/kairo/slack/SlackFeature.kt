package kairo.slack

import com.slack.api.Slack
import kairo.dependencyInjection.HasKoinModules
import kairo.feature.Feature
import kairo.protectedString.ProtectedString
import org.koin.core.module.Module
import org.koin.dsl.module

public class SlackFeature(
  config: SlackFeatureConfig,
) : Feature(), HasKoinModules {
  override val name: String = "Slack"

  override val koinModules: List<Module> =
    listOf(
      module {
        single {
          @OptIn(ProtectedString.Access::class)
          Slack.getInstance().methodsAsync(config.token.value)
        }
      },
    )
}

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
          val slack = Slack.getInstance()
          @OptIn(ProtectedString.Access::class)
          return@single SlackClient(
            asyncMethodsClient = slack.methodsAsync(config.token.value),
            channels = config.channels,
          )
        }
      },
    )
}

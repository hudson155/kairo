package kairo.slack

import com.slack.api.Slack
import kairo.dependencyInjection.HasKoinModules
import kairo.feature.Feature
import kairo.feature.LifecycleHandler
import kairo.feature.lifecycle
import org.koin.core.module.Module
import org.koin.dsl.module

public class SlackFeature(
  config: SlackFeatureConfig,
) : Feature(), HasKoinModules {
  override val name: String = "Slack"

  private val slackClient: SlackClient by lazy {
    val slack = Slack.getInstance()
    return@lazy SlackClient(
      slack = slack,
      token = config.token,
      channels = config.channels,
    )
  }

  override val koinModules: List<Module> =
    listOf(
      module {
        single { slackClient }
      },
    )

  override val lifecycle: List<LifecycleHandler> =
    lifecycle {
      handler {
        start { slackClient }
        stop { slackClient.close() }
      }
    }
}

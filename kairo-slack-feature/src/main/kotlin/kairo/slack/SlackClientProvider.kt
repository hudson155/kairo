package kairo.slack

import com.google.inject.Inject
import com.google.inject.Singleton
import com.slack.api.Slack
import kairo.dependencyInjection.LazySingletonProvider

/**
 * There is a single global [SlackClient] instance in Kairo.
 */
@Singleton
public class SlackClientProvider @Inject constructor(
  private val config: KairoSlackConfig,
  private val slack: Slack,
) : LazySingletonProvider<SlackClient>() {
  override fun create(): SlackClient =
    create(config)

  private fun create(config: KairoSlackConfig): SlackClient =
    when (config) {
      is KairoSlackConfig.Noop ->
        NoopSlackClient()
      is KairoSlackConfig.Real ->
        RealSlackClient(
          config = config,
          slack = slack,
        )
    }
}

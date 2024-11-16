package kairo.slack

import com.google.inject.Binder
import com.slack.api.Slack
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.toProvider

public open class KairoSlackFeature(
  private val config: KairoSlackConfig,
) : BaseKairoSlackFeature() {
  override fun bind(binder: Binder) {
    super.bind(binder)
    binder.bind<KairoSlackConfig>().toInstance(config)
    binder.bind<Slack>().toProvider(SlackProvider::class)
    binder.bind<SlackClient>().toProvider(SlackClientProvider::class)
  }
}

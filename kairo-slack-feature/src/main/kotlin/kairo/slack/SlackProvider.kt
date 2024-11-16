package kairo.slack

import com.google.inject.Inject
import com.google.inject.Singleton
import com.slack.api.Slack
import kairo.dependencyInjection.LazySingletonProvider

/**
 * There is a single global [Slack] instance in Kairo.
 */
@Singleton
public class SlackProvider @Inject constructor() : LazySingletonProvider<Slack>() {
  override fun create(): Slack =
    Slack.getInstance()
}

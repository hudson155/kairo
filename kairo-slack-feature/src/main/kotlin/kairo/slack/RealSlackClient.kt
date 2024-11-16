package kairo.slack

import com.slack.api.Slack
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.slack.api.model.block.LayoutBlock
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.protectedString.ProtectedString
import kotlinx.coroutines.future.await

private val logger: KLogger = KotlinLogging.logger {}

public class RealSlackClient(
  private val config: KairoSlackConfig.Real,
  private val slack: Slack,
) : SlackClient() {
  @OptIn(ProtectedString.Access::class)
  override suspend fun sendMessage(channelName: String, message: List<LayoutBlock>) {
    val channelId = deriveChannelId(channelName) ?: return
    logger.debug { "Sending message to channel: $channelName." }
    try {
      slack.methodsAsync(config.token.value).chatPostMessage(
        ChatPostMessageRequest.builder()
          .channel(channelId)
          .blocks(message)
          .mrkdwn(true)
          .build(),
      ).await()
    } catch (e: Exception) {
      logger.error(e) { "Failed to send Slack message." }
    }
  }

  private fun deriveChannelId(channelName: String): String? {
    val channelId = config.channels[channelName]
    if (channelId == null) {
      logger.error { "No Slack channel found: $channelName." }
    }
    return channelId
  }
}

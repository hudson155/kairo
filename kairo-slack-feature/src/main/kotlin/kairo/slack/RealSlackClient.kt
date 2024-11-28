package kairo.slack

import com.slack.api.Slack
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.slack.api.methods.response.chat.ChatPostMessageResponse
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
  override suspend fun sendMessage(
    channelName: String,
    threadTs: String?,
    message: List<LayoutBlock>,
  ): ChatPostMessageResponse? {
    val channelId = deriveChannelId(channelName) ?: return null
    logger.debug { "Sending message to channel: $channelName." }
    try {
      val response = slack.methodsAsync(config.token.value).chatPostMessage(
        ChatPostMessageRequest.builder()
          .channel(channelId)
          .threadTs(threadTs)
          .blocks(message)
          .mrkdwn(true)
          .build(),
      ).await()
      if (!response.isOk) {
        error(response.error)
      }
      return response
    } catch (e: Exception) {
      logger.error(e) { "Failed to send Slack message." }
      return null
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

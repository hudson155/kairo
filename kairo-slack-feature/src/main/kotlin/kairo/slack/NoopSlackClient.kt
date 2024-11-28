package kairo.slack

import com.slack.api.model.block.LayoutBlock

public class NoopSlackClient : SlackClient() {
  override suspend fun sendMessage(
    channelName: String,
    threadTs: String?,
    message: List<LayoutBlock>,
  ): Nothing? =
    null
}

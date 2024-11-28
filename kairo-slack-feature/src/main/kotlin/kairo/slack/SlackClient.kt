package kairo.slack

import com.slack.api.methods.response.chat.ChatPostMessageResponse
import com.slack.api.model.block.LayoutBlock

public abstract class SlackClient {
  public abstract suspend fun sendMessage(
    channelName: String,
    threadTs: String? = null,
    message: List<LayoutBlock>,
  ): ChatPostMessageResponse?
}

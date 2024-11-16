package kairo.slack

import com.slack.api.model.block.LayoutBlock

public abstract class SlackClient {
  public abstract suspend fun sendMessage(channelName: String, message: List<LayoutBlock>)
}

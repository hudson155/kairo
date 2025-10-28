package kairo.slack

import com.slack.api.methods.AsyncMethodsClient

public class SlackClient(
  asyncMethodsClient: AsyncMethodsClient,
  public val channels: Map<String, String>,
) : AsyncMethodsClient by asyncMethodsClient

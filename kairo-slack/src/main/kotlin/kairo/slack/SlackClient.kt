package kairo.slack

import com.slack.api.Slack
import com.slack.api.methods.AsyncMethodsClient
import kairo.protectedString.ProtectedString

@OptIn(ProtectedString.Access::class)
public class SlackClient(
  slack: Slack,
  token: ProtectedString,
  public val channels: Map<String, String>,
) : AsyncMethodsClient by slack.methodsAsync(token.value), AutoCloseable by slack

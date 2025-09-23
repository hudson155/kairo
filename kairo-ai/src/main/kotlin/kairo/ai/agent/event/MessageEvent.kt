package kairo.ai.agent.event

import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.data.message.UserMessage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed class MessageEvent : Event() {
  public abstract fun toMessage(): ChatMessage

  @Serializable
  @SerialName("AiMessage")
  public data class Ai(
    val agentName: String,
    val text: String,
    // TODO: Other fields
  ) : MessageEvent() {
    override fun toMessage(): AiMessage =
      AiMessage.from(text)

    public companion object {
      public fun from(agentName: String, message: AiMessage): Ai =
        Ai(
          agentName = agentName,
          text = message.text(),
        )
    }
  }

  @Serializable
  @SerialName("UserMessage")
  public data class User(
    val text: String,
  ) : MessageEvent() {
    override fun toMessage(): UserMessage =
      UserMessage.from(text)
  }
}

package kairo.ai.agent.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed class AgentEvent : Event() {
  @Serializable
  @SerialName("AgentStarted")
  public data class Started(
    val agentName: String,
  ) : AgentEvent()

  @Serializable
  @SerialName("AgentFinished")
  public data class Finished(
    val agentName: String,
    val success: Boolean,
  ) : Event()
}

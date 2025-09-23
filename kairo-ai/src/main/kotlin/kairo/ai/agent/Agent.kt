package kairo.ai.agent

import kairo.ai.agent.event.AgentEvent

public abstract class Agent<C : Context>(
  public val name: String,
) {
  public suspend fun process(context: C) {
    context.send(AgentEvent.Started(agentName = name))
    process2(context)
    context.send(AgentEvent.Finished(agentName = name, success = true)) // TODO try/catch error handling
  }

  protected abstract suspend fun process2(context: C)
}

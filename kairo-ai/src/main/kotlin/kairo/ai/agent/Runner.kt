package kairo.ai.agent

import kairo.ai.agent.event.Event
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

public abstract class Runner {
  public suspend fun <C : Context> run(agent: Agent<C>, context: C) {
    coroutineScope {
      launch {
        for (event in context) {
          consume(event)
        }
      }
      try {
        agent.process(context)
      } finally {
        context.close()
      }
    }
  }

  protected abstract suspend fun consume(event: Event)
}

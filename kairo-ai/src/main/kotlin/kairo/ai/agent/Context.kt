package kairo.ai.agent

import kairo.ai.agent.event.Event
import kotlinx.coroutines.channels.Channel

public abstract class Context : Channel<Event> by Channel()

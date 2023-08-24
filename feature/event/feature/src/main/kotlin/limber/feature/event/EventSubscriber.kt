package limber.feature.event

import kotlin.reflect.KClass

public typealias EventHandlerFunction<T> = (type: EventType, body: T) -> Unit

public abstract class EventSubscriber {
  public abstract class Factory {
    public abstract operator fun <T : Any> invoke(
      subscriptionName: String,
      kClass: KClass<T>,
      handler: EventHandlerFunction<T>,
    )

    internal abstract fun start()

    internal abstract fun stop()
  }
}

package limber.feature.event

import kotlin.reflect.KClass

public object FakeEventSubscriber : EventSubscriber() {
  public class Factory : EventSubscriber.Factory() {
    override fun <T : Any> invoke(
      topicName: String,
      subscriptionName: String,
      kClass: KClass<T>,
      handler: EventHandlerFunction<T>,
    ): Unit = Unit

    override fun start(): Unit = Unit

    override fun stop(): Unit = Unit
  }
}

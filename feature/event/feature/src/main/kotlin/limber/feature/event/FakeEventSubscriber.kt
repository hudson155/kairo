package limber.feature.event

import com.google.inject.Inject
import kotlin.reflect.KClass

public object FakeEventSubscriber : EventSubscriber() {
  public class Factory @Inject constructor() : EventSubscriber.Factory() {
    override fun <T : Any> invoke(
      subscriptionName: String,
      kClass: KClass<T>,
      handler: EventHandlerFunction<T>,
    ): Unit = Unit

    override fun start(): Unit = Unit

    override fun stop(): Unit = Unit
  }
}

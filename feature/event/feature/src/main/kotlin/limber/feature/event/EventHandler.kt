package limber.feature.event

import com.google.inject.Inject
import kotlin.reflect.KClass

public abstract class EventHandler<in T : Any>(
  internal val subscriptionName: String,
  private val kClass: KClass<T>,
) {
  @Inject
  private lateinit var subscriber: EventSubscriber.Factory

  internal fun init() {
    subscriber(subscriptionName, kClass, ::handle)
  }

  public abstract fun handle(type: EventType, body: T)
}

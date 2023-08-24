package limber.feature.event

import com.google.inject.Inject
import kotlin.reflect.KClass

public abstract class EventHandler<in T : Any>(
  private val topicName: String,
  private val subscriptionName: String,
  private val kClass: KClass<T>,
) {
  @Inject
  private lateinit var subscriber: EventSubscriber.Factory

  internal val description: String = subscriptionDescription(topicName, subscriptionName)

  internal fun init() {
    subscriber(
      topicName = topicName,
      subscriptionName = subscriptionName,
      kClass = kClass,
      handler = ::handle,
    )
  }

  public abstract fun handle(type: EventType, body: T)
}

internal fun subscriptionDescription(topicName: String, subscriptionName: String): String =
  "$topicName.$subscriptionName"

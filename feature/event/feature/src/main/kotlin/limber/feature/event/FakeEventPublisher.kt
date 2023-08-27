package limber.feature.event

import com.google.inject.Inject
import mu.KLogger
import mu.KotlinLogging

public class FakeEventPublisher<in T : Any>(private val topicName: String) : EventPublisher<T>() {
  public class Factory @Inject constructor() : EventPublisher.Factory() {
    override fun <T : Any> invoke(topicName: String): EventPublisher<T> =
      FakeEventPublisher(topicName)

    override fun start(): Unit = Unit

    override fun stop(): Unit = Unit
  }

  private val logger: KLogger = KotlinLogging.logger {}

  override suspend fun publish(type: EventType, body: T) {
    logger.info { "Publishing (no-op) event to topic $topicName. Type is $type. $body." }
  }
}

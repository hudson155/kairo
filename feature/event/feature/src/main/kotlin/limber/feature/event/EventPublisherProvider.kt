package limber.feature.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.google.inject.Provider
import limber.config.event.EventConfig

internal class EventPublisherProvider @Inject constructor(
  private val config: EventConfig,
  private val objectMapper: ObjectMapper,
) : Provider<EventPublisher.Factory> {
  override fun get(): EventPublisher.Factory {
    if (config.subscribe == null) {
      return FakeEventPublisher.Factory()
    }
    var factory: EventPublisher.Factory = RealEventPublisher.Factory(
      config = config,
      objectMapper = objectMapper,
    )
    if (config.transactionAware) {
      factory = TransactionAwareEventPublisher.Factory(factory)
    }
    return factory
  }
}

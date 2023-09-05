package limber.feature.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.name.Named
import limber.config.event.EventConfig

internal class EventSubscriberProvider @Inject constructor(
  private val config: EventConfig,
  @Named(EVENT_FEATURE) private val objectMapper: ObjectMapper,
) : Provider<EventSubscriber.Factory> {
  override fun get(): EventSubscriber.Factory {
    if (config.subscribe == null) {
      return FakeEventSubscriber.Factory()
    }
    return RealEventSubscriber.Factory(
      config = config,
      objectMapper = objectMapper,
    )
  }
}

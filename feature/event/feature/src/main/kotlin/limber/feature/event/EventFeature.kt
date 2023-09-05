package limber.feature.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Injector
import com.google.inject.PrivateBinder
import com.google.inject.name.Names
import limber.config.event.EventConfig
import limber.feature.Feature
import limber.feature.FeaturePriority
import limber.feature.filterBindings
import limber.serialization.ObjectMapperFactory
import mu.KLogger
import mu.KotlinLogging

internal const val EVENT_FEATURE = "EVENT_FEATURE"

public open class EventFeature(
  private val config: EventConfig,
) : Feature() {
  private val logger: KLogger = KotlinLogging.logger {}

  final override val priority: FeaturePriority = FeaturePriority.Framework

  final override fun bind(binder: PrivateBinder) {
    bind(EventConfig::class.java).toInstance(config)

    bind(ObjectMapper::class.java)
      .annotatedWith(Names.named(EVENT_FEATURE))
      .toInstance(ObjectMapperFactory.builder(ObjectMapperFactory.Format.Json).build())

    bind(EventPublisher.Factory::class.java).toProvider(EventPublisherProvider::class.java).asEagerSingleton()
    expose(EventPublisher.Factory::class.java)

    bind(EventSubscriber.Factory::class.java).toProvider(EventSubscriberProvider::class.java).asEagerSingleton()
    expose(EventSubscriber.Factory::class.java)
  }

  override fun start(injector: Injector, features: Set<Feature>) {
    val eventHandlers: List<EventHandler<*>> = injector.filterBindings()
    eventHandlers.forEach { eventHandler ->
      logger.info { "Initializing event handler: ${eventHandler.description}." }
      eventHandler.init()
    }

    injector.getInstance(EventSubscriber.Factory::class.java).start()
  }

  override fun afterStart(injector: Injector) {
    injector.getInstance(EventPublisher.Factory::class.java).start()
  }

  final override fun stop(injector: Injector?) {
    injector?.let { it.getInstance(EventPublisher.Factory::class.java).stop() }
    injector?.let { it.getInstance(EventSubscriber.Factory::class.java).stop() }
  }
}

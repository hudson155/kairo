package limber.feature.event

import com.google.inject.Injector
import com.google.inject.PrivateBinder
import limber.config.event.EventConfig
import limber.feature.Feature
import limber.feature.FeaturePriority
import limber.feature.filterBindings
import mu.KLogger
import mu.KotlinLogging
import kotlin.reflect.KClass

public open class EventFeature(
  private val config: EventConfig,
  private val publisher: KClass<out EventPublisher.Factory>,
  private val subscriber: KClass<out EventSubscriber.Factory>,
) : Feature() {
  private val logger: KLogger = KotlinLogging.logger {}

  final override val priority: FeaturePriority = FeaturePriority.Framework

  public constructor(config: EventConfig) : this(
    config = config,
    publisher = when (config.publish) {
      null -> FakeEventPublisher.Factory::class
      else -> RealEventPublisher.Factory::class
    },
    subscriber = when (config.subscribe) {
      null -> FakeEventSubscriber.Factory::class
      else -> RealEventSubscriber.Factory::class
    },
  )

  final override fun bind(binder: PrivateBinder) {
    bind(EventConfig::class.java).toInstance(config)

    bind(EventPublisher.Factory::class.java).to(publisher.java).asEagerSingleton()
    expose(EventPublisher.Factory::class.java)

    bind(EventSubscriber.Factory::class.java).to(subscriber.java).asEagerSingleton()
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

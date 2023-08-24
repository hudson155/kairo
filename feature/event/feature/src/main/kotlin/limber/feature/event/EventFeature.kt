package limber.feature.event

import com.google.inject.Injector
import com.google.inject.PrivateBinder
import limber.config.sql.EventConfig
import limber.feature.Feature
import limber.feature.FeaturePriority
import kotlin.reflect.KClass

public open class EventFeature(
  private val config: EventConfig,
  private val publisher: KClass<out EventPublisher.Factory>,
) : Feature() {
  final override val priority: FeaturePriority = FeaturePriority.Framework

  public constructor(config: EventConfig) : this(
    config = config,
    publisher = when (config.publish) {
      null -> FakeEventPublisher.Factory::class
      else -> RealEventPublisher.Factory::class
    },
  )

  final override fun bind(binder: PrivateBinder) {
    bind(EventConfig::class.java).toInstance(config)

    bind(EventPublisher.Factory::class.java).to(publisher.java).asEagerSingleton()
    expose(EventPublisher.Factory::class.java)
  }

  override fun afterStart(injector: Injector) {
    injector.getInstance(EventPublisher.Factory::class.java).start()
  }

  final override fun stop(injector: Injector?) {
    injector?.let { it.getInstance(EventPublisher.Factory::class.java).stop() }
  }
}

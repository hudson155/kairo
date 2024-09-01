package kairo.feature

import com.google.inject.AbstractModule
import com.google.inject.Binder
import com.google.inject.Injector
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Features are Kairo's main building block.
 * Every piece of functionality,
 * from framework-level REST or SQL behaviour
 * to application-specific implementations
 * to third-party integrations
 * is represented by a Kairo Feature.
 */
public abstract class Feature : AbstractModule() {
  public abstract val name: String

  public abstract val priority: FeaturePriority

  final override fun configure() {
    val binder = binder()
    binder.configureKairoBinder()
    logger.info { "Binding Feature: $name." }
    bind(binder)
  }

  /**
   * Primarily for dependency injection,
   * the implementation should bind everything for the Feature.
   * It should not initialize anything stateful (for example, connection pools).
   */
  public open fun bind(binder: Binder): Unit = Unit

  /**
   * Startup work; initializing state (such as connection pools).
   */
  public open fun start(injector: Injector, features: Set<Feature>): Unit = Unit

  /**
   * Change state after startup.
   */
  public open fun afterStart(injector: Injector): Unit = Unit

  /**
   * Change state before shutdown.
   * [injector] may be null if the Server did not start correctly.
   */
  public open fun beforeStop(injector: Injector?): Unit = Unit

  /**
   * Clean up resources.
   * [injector] may be null if the Server did not start correctly.
   */
  public open fun stop(injector: Injector?): Unit = Unit
}

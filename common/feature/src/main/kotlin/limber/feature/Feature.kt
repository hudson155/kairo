package limber.feature

import com.google.inject.Injector
import com.google.inject.PrivateBinder
import com.google.inject.PrivateModule

public abstract class Feature : PrivateModule() {
  public abstract val priority: FeaturePriority

  final override fun configure() {
    val binder = binder()
    binder.requireAtInjectOnConstructors()
    binder.bind()
  }

  /**
   * Primarily for dependency injection,
   * the implementation should bind everything for the feature.
   * It should not initialize anything stateful (such as connection pools).
   */
  public abstract fun PrivateBinder.bind()

  /**
   * Startup work; initializing state (such as connection pools).
   */
  public open fun start(injector: Injector, features: Set<Feature>): Unit =
    Unit

  /**
   * Change state after startup.
   */
  public open fun afterStart(injector: Injector): Unit =
    Unit

  /**
   * Change state before shutdown.
   */
  public open fun beforeStop(injector: Injector): Unit =
    Unit

  /**
   * Clean up resources.
   */
  public open fun stop(): Unit =
    Unit
}

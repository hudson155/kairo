package kairo.feature

import com.google.inject.Injector
import com.google.inject.PrivateBinder
import com.google.inject.PrivateModule

public abstract class Feature : PrivateModule() {
  public abstract val name: String

  public abstract val priority: FeaturePriority

  final override fun configure() {
    val binder = binder()
    binder.configureKairoBinder()
    bind(binder)
  }

  /**
   * Primarily for dependency injection,
   * the implementation should bind everything for the Feature.
   * It should not initialize anything stateful (e.g. connection pools).
   */
  public open fun bind(binder: PrivateBinder): Unit = Unit

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

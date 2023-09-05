package limber.feature

import com.google.inject.Binder
import com.google.inject.Injector
import com.google.inject.PrivateBinder
import com.google.inject.PrivateModule

/**
 * An Application is composed of a set of Features,
 * each of which enables some specific functionality.
 *
 * See the root README for more detail.
 */
public abstract class Feature : PrivateModule() {
  public val name: String = checkNotNull(this::class.simpleName)

  public abstract val priority: FeaturePriority

  final override fun configure() {
    val binder = binder()
    binder.configureHighbeamBinder()
    bind(binder)
  }

  /**
   * Primarily for dependency injection,
   * the implementation should bind everything for the Feature.
   * It should not initialize anything stateful (such as connection pools).
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

public fun Binder.configureHighbeamBinder() {
  requireAtInjectOnConstructors()
  requireExactBindingAnnotations()
}

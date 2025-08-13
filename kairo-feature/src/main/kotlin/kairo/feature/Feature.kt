package kairo.feature

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * TODO: Add KDoc.
 */
public abstract class Feature {
  /**
   * Feature names should be unique.
   */
  public abstract val name: String

  /**
   * Called during various lifecycle events.
   * This method is NOT called concurrently with other Features.
   * The implementation is expected to launch and return a [Job] to do async work.
   * All Features' jobs will be awaited before proceeding, but they can proceed in parallel.
   */
  public open fun CoroutineScope.on(event: LifecycleEvent): Job? =
    null
}

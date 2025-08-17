package kairo.application

import arrow.continuations.SuspendApp
import arrow.fx.coroutines.ResourceScope
import arrow.fx.coroutines.resourceScope
import kairo.server.Server
import kotlinx.coroutines.awaitCancellation

public class Kairo internal constructor(
  resourceScope: ResourceScope,
) : ResourceScope by resourceScope {
  /**
   * Starts the Server and waits for JVM termination.
   */
  public suspend fun Server.startAndWait(
    /**
     * Called when the JVM terminates.
     * Should call [Server.stop].
     * Can do other things too.
     */
    release: suspend () -> Unit,
  ) {
    install(
      acquire = { start() },
      release = { _, _ -> release() },
    )
    awaitCancellation()
  }
}

/**
 * An optional way to run your [Server] instance.
 */
public fun kairo(block: suspend Kairo.() -> Unit) {
  SuspendApp {
    resourceScope {
      val kairo = Kairo(this)
      kairo.block()
    }
  }
}

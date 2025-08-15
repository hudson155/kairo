package kairo.server.runner

import arrow.continuations.SuspendApp
import arrow.fx.coroutines.ResourceScope
import arrow.fx.coroutines.resourceScope
import kairo.server.Server
import kotlinx.coroutines.awaitCancellation

/**
 * An optional way to run your [Server] instance.
 * Automatically starts and waits for JVM termination.
 */
public class Kairo internal constructor(
  resourceScope: ResourceScope,
) : ResourceScope by resourceScope {
  public suspend fun Server.startAndWait(
    release: suspend () -> Unit,
  ) {
    install(
      acquire = { start() },
      release = { _, _ -> release() },
    )
    awaitCancellation()
  }
}

public fun kairo(block: suspend Kairo.() -> Unit) {
  SuspendApp {
    resourceScope {
      val kairo = Kairo(this)
      kairo.block()
    }
  }
}

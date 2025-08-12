package kairo.cyclicBarrier

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Kotlin coroutine equivalent of Java's [java.util.concurrent.CyclicBarrier].
 * See https://en.wikipedia.org/wiki/Barrier_(computer_science) to understand barriers.
 * Called "cyclic" because it can be re-used after being broken.
 */
public class CyclicBarrier(
  /**
   * The number of parties that must call [await] before the barrier breaks.
   */
  private val parties: Int,
  /**
   * This command will be executed after the barrier is broken,
   * before any of the parties are released.
   */
  private val barrierCommand: (suspend () -> Unit)? = null,
) {
  @Suppress("UseDataClass")
  private class Generation(var broken: Boolean)

  private val lock: Mutex = Mutex()
  private var generation: Generation = Generation(broken = false)
  private var waiting: Int = 0
  private var gate: CompletableDeferred<Unit> = CompletableDeferred()

  init {
    require(parties > 0) { "Parties must be positive." }
  }

  public suspend fun await() {
    val generation: Generation
    val gate: CompletableDeferred<Unit>

    lock.withLock {
      this.waiting++
      if (this.waiting == this.parties) {
        breakBarrier()
        return
      } else {
        generation = this.generation
        gate = this.gate
      }
    }

    try {
      gate.await()
    } catch (e: Throwable) {
      handleException(e, generation)
    }
  }

  private suspend fun breakBarrier() {
    val gate = this.gate
    nextGeneration()
    if (this.barrierCommand == null) {
      gate.complete(Unit)
      return
    }
    try {
      this.barrierCommand()
      gate.complete(Unit)
    } catch (e: Throwable) {
      gate.completeExceptionally(e)
      throw e
    }
  }

  private suspend fun handleException(e: Throwable, generation: Generation): Nothing {
    var gate: CompletableDeferred<Unit>? = null
    lock.withLock {
      if (this.generation === generation && !this.generation.broken) {
        this.generation.broken = true
        gate = this.gate
        nextGeneration()
      }
    }
    gate?.completeExceptionally(e)
    throw e
  }

  private fun nextGeneration() {
    this.generation = Generation(broken = false)
    this.waiting = 0
    this.gate = CompletableDeferred()
  }
}
